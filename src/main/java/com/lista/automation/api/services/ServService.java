package com.lista.automation.api.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lista.automation.api.pojo.service.ServiceCreateRequest;
import com.lista.automation.api.utils.RestService;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ServService extends RestService {
    public ServService(String cookie) {
        super(cookie);
    }

    @Override
    protected String getBasePath() {
        return "catalog/services/";
    }

    @Step("api: post service")
    public int create(ServiceCreateRequest simpleService, int expectStatus) {
        int categoryID = Integer.parseInt(new CategoryService(getCookie()).createCategory(simpleService));

        Response response = given().spec(getREQ_SPEC_ENCODED()).log().ifValidationFails()
                .formParam("name", "service_"+simpleService.getServiceName())
                .formParam("duration", simpleService.getServiceDuration())
                .formParam("price", simpleService.getPrice())
                .formParam("color", "#efa5fe")
                .formParam("category_id", categoryID)
                .when().post();

        return Integer.parseInt(response.then().log().ifError().statusCode(expectStatus)
                .extract().body().htmlPath().get().toString());
    }
    @Step("api: get services")
    public List<ServiceCreateRequest> getServiceList(int expectStatus) {
        Response response = given().spec(getREQ_SPEC_ENCODED()).log().all().get();

        List<ServiceCreateRequest> services = response.then().log().all().statusCode(expectStatus)
                .extract().body().jsonPath().getList("", ServiceCreateRequest.class);

        if (services.isEmpty()) {
            throw new RuntimeException("Services were not found");
        }
        return response.then().log().all().statusCode(expectStatus)
                .extract().body().jsonPath().getList("", ServiceCreateRequest.class);
    }
    @Step("api: get service by ID")
    public List<ServiceCreateRequest> getServiceByID(int serviceID, int expectStatus) {
        Response response = given().spec(getREQ_SPEC_ENCODED()).log().all().get();

        List<ServiceCreateRequest> services = response.then().log().all().statusCode(expectStatus)
                .extract().body().jsonPath().getList("", ServiceCreateRequest.class);

        List<ServiceCreateRequest> service = services.stream().filter(s -> s.getServiceID() == serviceID).collect(Collectors.toList());
        if (service.isEmpty()) {
            throw new RuntimeException("Can't find service by ID = '"+serviceID+"'");
        }
        return service;
    }

    @Step("objectMapper: convert Service to Json")
    public List<String> convertServiceToJson(List<ServiceCreateRequest> serviceByName) throws JsonProcessingException {
        if (serviceByName.isEmpty()) {
            throw new RuntimeException("Can't convert Services to json - list of services pojo class is empty");
        }
        Map<String, Object> category = new HashMap<>();
        category.put("name", serviceByName.get(0).getCategory().getCategoryName());
        category.put("id", serviceByName.get(0).getCategory().getId());

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", serviceByName.get(0).getServiceID());
        jsonMap.put("name", serviceByName.get(0).getServiceName());
        jsonMap.put("price", serviceByName.get(0).getPrice());
        jsonMap.put("duration", serviceByName.get(0).getServiceDuration());
        jsonMap.put("color", serviceByName.get(0).getColor());
        jsonMap.put("category", category);
        jsonMap.put("count", 1);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(Collections.singletonList(jsonMap));

        List<String>serviceJsonList = new ArrayList<>();
        serviceJsonList.add(jsonString);
        return serviceJsonList;
    }
    public void deleteAll() {
        List<ServiceCreateRequest> serviceList = getServiceList(200);
        List<Integer> serviceIDs = serviceList.stream().map(ServiceCreateRequest::getServiceID).collect(Collectors.toList());

//        serviceIDs.forEach(s-> {
//            given().spec(getSPEC_ENCODED_ID(s)).log().all()
//                    .delete().then().log().all().statusCode(204);
//        });

        for(int i: serviceIDs) {
            given().spec(getSPEC_ENCODED_ID(i)).log().all()
                    .delete().then().log().all().statusCode(204);
        }
    }

    public static class CategoryService extends RestService {

        public CategoryService(String cookie) {
            super(cookie);
        }

        public String createCategory(ServiceCreateRequest simpleService) {
            Response response = given().spec(getREQ_SPEC_ENCODED()).log().all()
                    .formParam("name", "category_" + simpleService.getServiceName())
                    .when().post();

            return response.then().statusCode(201).extract().body().htmlPath().get().toString();
        }

        @Override
        protected String getBasePath() {
            return "catalog/services/categories";
        }
    }
}

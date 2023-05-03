package com.lista.automation.api.utils;

import com.lista.automation.api.pojo.service.ServiceCreateRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.List;
import static io.restassured.RestAssured.given;

public class ServService extends RestService {
    public ServService(String cookie) {
        super(cookie);
    }

    @Override
    protected String getBasePath() {
        return "catalog/services";
    }

    @Step("api: post service")
    public String create(ServiceCreateRequest simpleService, int expectStatus) {
        int categoryID = Integer.parseInt(new CategoryService(getCookie()).createCategory(simpleService));

        Response response = given().spec(REQ_SPEC_ENCODED).log().all()
                .formParam("name", "service_"+simpleService.getName())
                .formParam("duration", simpleService.getDuration())
                .formParam("price", simpleService.getPrice())
                .formParam("color", "#efa5fe")
                .formParam("category_id", categoryID)
                .when().post();

        return response.then().statusCode(expectStatus).extract().body().htmlPath().get().toString();
    }
    @Step("api: get services")
    public List<ServiceCreateRequest> getServiceList(int expectStatus) {
        Response response = given().spec(REQ_SPEC_ENCODED).log().all().get();
        return response.then().log().all().statusCode(expectStatus)
                .extract().body().jsonPath().getList("", ServiceCreateRequest.class);
    }

    public static class CategoryService extends RestService {

        public CategoryService(String cookie) {
            super(cookie);
        }

        public String createCategory(ServiceCreateRequest simpleService) {
            Response response = given().spec(REQ_SPEC_ENCODED).log().all()
                    .formParam("name", "category_" + simpleService.getName())
                    .when().post();

            return response.then().statusCode(201).extract().body().htmlPath().get().toString();
        }

        @Override
        protected String getBasePath() {
            return "catalog/services/categories";
        }
    }
}

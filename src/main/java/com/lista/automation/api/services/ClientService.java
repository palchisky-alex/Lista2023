package com.lista.automation.api.services;

import com.lista.automation.api.Properties;
import com.lista.automation.api.assert_response.RestResponse;
import com.lista.automation.api.assert_response.VerifyClientResponse;
import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.api.pojo.client.ClientGetResponse;
import com.lista.automation.api.utils.RestService;
import io.qameta.allure.Step;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static io.restassured.RestAssured.given;

public class ClientService extends RestService {
    public ClientService(String cookie) {
        super(cookie);
    }

    @Override
    protected String getBasePath() {
        return "clients";
    }

    @Step("api: client post")
    public String create(ClientCreateRequest client, int expectStatus) {
        Response response = given().spec(getREQ_SPEC_FORM()).log().all()
                .multiPart("phone", client.getPhone())
                .multiPart("name", client.getName())
                .multiPart("email", client.getEmail())
                .multiPart("address", client.getAddress())
                .multiPart("debts", client.getDebts())
                .multiPart("notes", client.getNotes())
                .multiPart("status", client.getStatus())
                .multiPart("birthdate", client.getBirthdate())
                .multiPart("birthyear", client.getBirthyear())
                .multiPart("gender", client.getGender())
                .when().post();

        return response.then().statusCode(expectStatus).extract().body().htmlPath().get().toString();
    }

    public RestResponse<List<ClientGetResponse>>findListNew() {
        return new RestResponse<>(
                given().spec(getREQ_SPEC_FORM()).log().all()
                        .param("limit", 40)
                        .param("offset", 0)
                        .get(),
                resp -> resp.body().jsonPath().getList("", ClientGetResponse.class)
        );
    }

    @Step("api: get list of clients")
    public List<ClientGetResponse> findList(int expectStatus) {
        return given().spec(getREQ_SPEC_FORM()).log().all()
                .param("limit", 40)
                .param("offset", 0)
                .get()
                .then().log().all().statusCode(expectStatus)
                .extract().body().jsonPath().getList("", ClientGetResponse.class);
    }

    @Step("api: get client by")
    public ClientGetResponse find(String findBy) {
        Response response= given().spec(getREQ_SPEC_FORM()).log().all()
                .param("limit", 40)
                .param("offset", 0)
                .param("q", findBy)
                .get()
                .then().log().all().extract().response();

        VerifyClientResponse.assertThat(response)
                .statusCodeIs(200)
                .matchesSchema(Properties.getProp().schemaClient())
                .assertAll();

        List<ClientGetResponse> clientList = response.then().extract().body().jsonPath().getList("", ClientGetResponse.class);
        if (clientList.isEmpty()) {
            throw new RuntimeException("Client ['"+findBy+"'] not found - list of clients is empty");
        }

        return response.then().extract().body().jsonPath().getList("", ClientGetResponse.class).get(0);
    }

    @Step("api: delete client by id")
    public void delete(String id, int expectStatus) {
        given().spec(getREQ_SPEC_ENCODED()).log().all()
                .formParam(getBasePath(), id).log().all()
                .delete().then().log().all().statusCode(expectStatus);
    }

    @Step("api: delete all clients")
    public void deleteAll(int expectStatus) {
        List<ClientGetResponse> clientList = findList(200);
        while (clientList.size() > 0) {
            List<String> IDs = clientList.stream().map(ClientGetResponse::getId).collect(Collectors.toList());

            given().spec(getREQ_SPEC_ENCODED()).log().all()
                    .formParam(getBasePath(), String.join(",", IDs)).log().all()
                    .delete().then().log().all().statusCode(expectStatus);
            clientList = findList(200);
        }
    }

    private Response getResponse(ClientCreateRequest client) {
        return given().spec(getREQ_SPEC_FORM()).log().all()
                .multiPart("phone", client.getPhone())
                .multiPart("name", client.getName())
                .multiPart("email", client.getEmail())
                .multiPart("address", client.getAddress())
                .multiPart("debts", client.getDebts())
                .multiPart("notes", client.getNotes())
                .multiPart("status", client.getStatus())
                .multiPart("birthdate", client.getBirthdate())
                .multiPart("birthyear", client.getBirthyear())
                .multiPart("gender", client.getGender())
                .when().post();
    }


}

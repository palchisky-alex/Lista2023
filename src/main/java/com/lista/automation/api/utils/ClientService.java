package com.lista.automation.api.utils;

import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.api.pojo.client.ClientGetResponse;
import io.restassured.response.Response;

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

    public String create(ClientCreateRequest client, int expectStatus) {
        Response response = given().spec(REQ_SPEC_FORM).log().all()
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

    public List<ClientGetResponse> findList(int expectStatus) {
        return given().spec(REQ_SPEC_FORM).log().all()
                .param("limit", 40)
                .param("offset", 0)
                .get()
                .then().log().all().statusCode(expectStatus)
                .extract().body().jsonPath().getList("", ClientGetResponse.class);
    }

    public ClientGetResponse find(String findBy, int expectStatus) {
        List<ClientGetResponse> clientList = given().spec(REQ_SPEC_FORM).log().all()
                .param("limit", 40)
                .param("offset", 0)
                .param("q", findBy)
                .get()
                .then().log().all().statusCode(expectStatus)
                .extract().body().jsonPath().getList("", ClientGetResponse.class);

        if (clientList.isEmpty()) {
            throw new RuntimeException("Client ['"+findBy+"'] not found - сlient list is empty");
        }
        return clientList.get(0);
    }

    public void delete(String id, int expectStatus) {
        given().spec(REQ_SPEC_ENCODED).log().all()
                .formParam(getBasePath(), id).log().all()
                .delete().then().log().all().statusCode(expectStatus);
    }

    public void deleteAll(int expectStatus) {
        List<ClientGetResponse> clientList = findList(200);
        while (clientList.size() > 0) {
            List<String> IDs = clientList.stream().map(ClientGetResponse::getId).collect(Collectors.toList());

            given().spec(REQ_SPEC_ENCODED).log().all()
                    .formParam(getBasePath(), String.join(",", IDs)).log().all()
                    .delete().then().log().all().statusCode(expectStatus);
            clientList = findList(200);
        }
    }

    private Response getResponse(ClientCreateRequest client) {
        return given().spec(REQ_SPEC_FORM).log().all()
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

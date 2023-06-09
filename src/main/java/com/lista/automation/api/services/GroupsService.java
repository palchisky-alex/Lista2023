package com.lista.automation.api.services;

import com.lista.automation.api.pojo.client.ClientGetResponse;
import com.lista.automation.api.pojo.group.GroupCreateRequest;
import com.lista.automation.api.pojo.group.GroupsGetResponse;
import com.lista.automation.api.utils.RestService;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class GroupsService extends RestService {
    public GroupsService(String cookie) {
        super(cookie);
    }

    @Override
    protected String getBasePath() {
        return "groups/";
    }

    @Step("api: group post")
    public int create(GroupCreateRequest group, int expectStatus) {
        ValidatableResponse response = given().spec(getREQ_SPEC_ENCODED()).log().all()
                .formParam("clients", "[]")
                .formParam("name", group.getName()).log().all()
                .post().then().log().all().statusCode(expectStatus);
        return Integer.parseInt(response.extract().body().htmlPath().get().toString());
    }

    @Step("api: group delete by id")
    public void delete(int id, int expectStatus) {
        given().spec(getSPEC_ENCODED_ID(id)).log().all()
                .delete().then().log().all().statusCode(expectStatus);
    }

    @Step("api: delete all client groups")
    public void deleteAll(int expectStatus) {
        List<GroupsGetResponse> clientGroups = getGroupsOfClient();
        clientGroups.forEach(g -> delete(g.getId(), expectStatus));
    }

    @Step("api: get list of all groups")
    public List<GroupsGetResponse> getAllGroups(int expectStatus) {
        return given().spec(getREQ_SPEC_FORM()).log().all()
                .get()
                .then().log().all().statusCode(expectStatus)
                .extract().body().jsonPath().getList("", GroupsGetResponse.class);
    }

    @Step("api: get clients in group")
    public List<ClientGetResponse> getClientsOfGroup(int id, String path, int expectStatus) {
        return given().spec(getSPEC_ENCODED_PATH(id, path, ContentType.URLENC)).log().all()
                .get().then().log().all().statusCode(expectStatus)
                .extract().body().jsonPath().getList("", ClientGetResponse.class);
    }

    @Step("api: put clients into group")
    public void putClientIntoGroup(int groupID, String path, String clientID, int expectStatus) {
        given().spec(getSPEC_ENCODED_PATH(groupID, path, ContentType.JSON)).log().all().body("[" + clientID + "]")
                .put().then().log().all().statusCode(expectStatus);
    }

    @Step("api: get list of client groups")
    public List<GroupsGetResponse> getGroupsOfClient() {
        List<GroupsGetResponse> groups = getAllGroups(200);
        return groups.stream().filter(group -> !group.isAutomatic()).collect(Collectors.toList());
    }

    @Step("api: get list of automatic groups")
    public List<GroupsGetResponse> getAutomaticGroups() {
        List<GroupsGetResponse> groups = getAllGroups(200);
        return groups.stream().filter(GroupsGetResponse::isAutomatic).collect(Collectors.toList());
    }

}

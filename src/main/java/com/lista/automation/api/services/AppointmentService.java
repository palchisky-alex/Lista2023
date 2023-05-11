package com.lista.automation.api.services;

import com.lista.automation.api.pojo.appointment.AppointmentCreateResponse;
import com.lista.automation.api.pojo.appointment.AppointmentGetRequest;
import com.lista.automation.api.pojo.client.ClientCreateRequest;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import com.lista.automation.api.utils.RestService;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.waitAtMost;

public class AppointmentService extends RestService {
    public AppointmentService(String cookie) {
        super(cookie);
    }

    @Override
    protected String getBasePath() {
        return "calendar";
    }

    @Step("api: post appointment")
    public String create(ClientCreateRequest client, String clientID, List<String> service, String dateTime, int expectStatus) {
        Response response;
        if(clientID.equals("-1")) {
            response = given().spec(getREQ_SPEC_ENCODED()).log().all()
                    .formParam("start", dateTime)
                    .formParam("client_id", clientID)
                    .formParam("worker_id", 1)
                    .formParam("total_price", 0)
                    .formParam("services", "[]")
                    .formParam("duration", 60)
                    .when().post();
        }
        else {

            response = given().spec(getREQ_SPEC_ENCODED()).log().all()
                    .formParam("start", dateTime)
                    .formParam("client_id", clientID)
                    .formParam("worker_id", 1)
                    .formParam("total_price", 10)
                    .formParam("services", service)
                    .formParam("note", client.getNotes())
                    .formParam("address", client.getAddress())
                    .formParam("duration", 60)
                    .formParam("phone", client.getPhone())
                    .when().post();
        }

        return response.then().statusCode(expectStatus).extract().body().htmlPath().get().toString();
    }

    @Step("api: get appointment by date")
    public List<AppointmentGetRequest> getAppointmentsByDate(String start, String end, int expectStatus) {
        return given().spec(getREQ_SPEC_ENCODED()).log().all()
                .param("start", start)
                .param("end", end)
                .param("worker_id", "1")
                .get()
                .then().log().all().statusCode(expectStatus)
                .extract().body().jsonPath().getList("", AppointmentGetRequest.class);
    }

    @Step("api: delete all today's appointments")
    public void deleteAll(String start, String end, int expectStatus)  {
        List<AppointmentGetRequest> appointmentList = getAppointmentsByDate(start, end, 200);

        List<Integer> appointmentIDs = appointmentList.stream()
                .map(AppointmentGetRequest::getAppointmentID).collect(Collectors.toList());


        for (int id : appointmentIDs) {
            AppointmentCreateResponse response = given().spec(getSPEC_ENCODED_ID_slash(id)).log().all()
                    .delete().then().log().all().statusCode(expectStatus)
                    .extract().body().jsonPath().getObject("", AppointmentCreateResponse.class);

            await().atMost(5, SECONDS).untilAsserted(() ->
                    assertThat(response).as("delete appointment response")
                            .extracting("isNotificationSent", "isSmsFailed")
                            .contains(false, false));

        }
    }

    }
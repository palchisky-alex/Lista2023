package com.lista.automation.api.utils;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

/**
 * Created by Palchitsky Alex
 */
public class CalendarSettingsService extends RestService {
    public CalendarSettingsService(String cookie) {
        super(cookie);
    }

    @Override
    protected String getBasePath() {
        return "settings/calendar/calendar_view";
    }

    @Step("api: put calendar settings")
    public void create(int expectStatus) {
        given().spec(REQ_SPEC_ENCODED).log().all()
                .formParam("calendar_view", "daily")
                .when().put().then().log().all().statusCode(expectStatus);

    }
}

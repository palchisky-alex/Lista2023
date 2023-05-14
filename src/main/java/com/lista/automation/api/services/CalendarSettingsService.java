package com.lista.automation.api.services;

import com.lista.automation.api.utils.RestService;
import io.qameta.allure.Step;
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
        return "settings/calendar";
    }

    @Step("api: put calendar settings - view")
    public void update(String view, String path) {
        given().spec(getSPEC_ENCODED_PATH(path)).log().all()
                .formParam(path, view)
                .when().put().then().log().all().statusCode(204);

    }
}

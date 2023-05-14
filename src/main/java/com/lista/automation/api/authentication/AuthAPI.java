package com.lista.automation.api.authentication;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import static com.lista.automation.api.Properties.getProp;
import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;

public class AuthAPI {

    public static ValidatableResponse getToken(AuthPojo auth) {

        return   given().log().all()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .config(RestAssured.config().encoderConfig(encoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))

                .baseUri(getProp().baseURL())
                .basePath("check-login")
                .header("referer", getProp().baseURL()+"/en/login")

                .formParam("time_zone",auth.getTimeZone())
                .formParam("email",auth.getEmail())
                .formParam("current-password",auth.getCurrentPassword())

                .post()
                .then().log().all().statusCode(302);
    }
}

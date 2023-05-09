package com.lista.automation.api.authentication;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import static com.lista.automation.api.Properties.getProp;
import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;

public class AuthAPI {

    public static ValidatableResponse login() {
        AuthPojo field = new AuthPojo("Asia/Jerusalem", getProp().username(), getProp().password());

        return   given().log().all()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .config(RestAssured.config().encoderConfig(encoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))

                .baseUri(getProp().baseURL())
                .basePath("check-login")
                .header("referer", getProp().baseURL()+"/en/login")

                .formParam("time_zone",field.getTimeZone())
                .formParam("email",field.getEmail())
                .formParam("current-password",field.getCurrentPassword())

                .post()
                .then().log().all().statusCode(302);
    }
}

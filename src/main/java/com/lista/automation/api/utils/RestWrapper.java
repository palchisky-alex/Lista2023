package com.lista.automation.api.utils;
import com.lista.automation.api.pojo.UserLoginRequest;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import static com.lista.automation.api.utils.RestService.BASE_URL;
import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.hamcrest.MatcherAssert.assertThat;

public class RestWrapper {
    private String cookie;
    public ClientService client;
    public GroupsService group;

    public RestWrapper(String cookie) {
        this.cookie = cookie;
        client = new ClientService(cookie);
        group = new GroupsService(cookie);
    }

    public static RestWrapper loginAs(String login, String pass) {
        String key = "5531a58348162222";
        UserLoginRequest field = new UserLoginRequest("Asia/Jerusalem", login, pass);


        ValidatableResponse response =  given().log().all()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .config(RestAssured.config().encoderConfig(encoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))

                .baseUri(BASE_URL)
                .basePath("check-login")
                .header("referer", "https://test.atzma.im/en/login")

                .formParam("time_zone",field.getTimeZone())
                .formParam("email",field.getEmail())
                .formParam("current-password",field.getCurrentPassword())

                .post()
                .then().log().all().statusCode(302);

        String myCookie = response.extract().cookie(key);

        return new RestWrapper(key + "=" + myCookie);
    }
}

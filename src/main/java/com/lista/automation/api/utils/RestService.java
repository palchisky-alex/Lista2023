package com.lista.automation.api.utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.config.EncoderConfig.encoderConfig;

public abstract class RestService {

    protected static final String BASE_URL = "https://test.atzma.im/";
    protected RequestSpecification REQ_SPEC_FORM;
    protected RequestSpecification REQ_SPEC_ENCODED;
    protected RequestSpecification REQ_SPEC_ENCODED_DEL;
    private String cookie;
    protected abstract String getBasePath();

    public RestService(String cookie) {
        this.cookie = cookie;
        REQ_SPEC_FORM = new RequestSpecBuilder()
                .addHeader("cookie", cookie)
                .setBaseUri(BASE_URL)
                .setBasePath(getBasePath())
                .setContentType(ContentType.MULTIPART)
                .build();

        REQ_SPEC_ENCODED = new RequestSpecBuilder()
                .addHeader("cookie", cookie)
                .setBaseUri(BASE_URL)
                .setBasePath(getBasePath())
                .setContentType(ContentType.URLENC).setConfig(RestAssured.config().encoderConfig(encoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .build();

    }

    public RequestSpecification getSPEC_ENCODED_DEL(int id) {
        REQ_SPEC_ENCODED = new RequestSpecBuilder()
                .addHeader("cookie", cookie)
                .setBaseUri(BASE_URL)
                .setBasePath(getBasePath()+id)
                .setContentType(ContentType.URLENC).setConfig(RestAssured.config().encoderConfig(encoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .build();
        return REQ_SPEC_ENCODED;
    }


    public String getCookie() {
        return cookie;
    }

    public boolean checkStatusCode(int status) {
        if (status == 401) {
            // Update access token and resend the request
            RestWrapper.loginAs("alex.palchisky@gmail.com", "123456");
            return false;
        }
        return true;
    }

    public static RequestSpecification requestSpecification(String basePath) {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setBasePath(basePath)
                .setContentType(ContentType.MULTIPART)
                .build();
    }

    public static ResponseSpecification responseSpecificationStatus(int status) {
        return new ResponseSpecBuilder()
                .expectStatusCode(status).build();
    }

    public static void installSpecifications(ResponseSpecification response) {
//        RestAssured.requestSpecification = request;
        RestAssured.responseSpecification = response;
    }
}

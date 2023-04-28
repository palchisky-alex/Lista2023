package com.lista.automation.api.utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public abstract class RestService {

    protected static final String BASE_URL = "https://test.atzma.im/";
    protected RequestSpecification REQ_SPEC;
    private String cookie;
    protected abstract String getBasePath();

    public RestService(String cookie) {
        this.cookie = cookie;
        REQ_SPEC = new RequestSpecBuilder()
                .addHeader("cookie", cookie)
                .setBaseUri(BASE_URL)
                .setBasePath(getBasePath())
                .setContentType(ContentType.MULTIPART)
                .build();
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

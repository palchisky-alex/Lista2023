package com.lista.automation.api.utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static com.lista.automation.api.Properties.getProp;
import static io.restassured.config.EncoderConfig.encoderConfig;

public abstract class RestService {

    public final String BASE_URL = getProp().baseURL();
    private RequestSpecification REQ_SPEC_FORM;
    private RequestSpecification REQ_SPEC_HTML;
    private RequestSpecification REQ_SPEC_ENCODED;
    private RequestSpecification REQ_SPEC_ENCODED_PATH;
    private RequestSpecification REQ_SPEC_ENCODED_ID;
    private String cookie;

    protected abstract String getBasePath();

    public RestService(String cookie) {
        this.cookie = cookie;
    }


    public RequestSpecification getREQ_SPEC_ENCODED() {
        if (REQ_SPEC_ENCODED == null) {
            REQ_SPEC_ENCODED = new RequestSpecBuilder()
                    .addHeader("cookie", cookie)
                    .setBaseUri(BASE_URL)
                    .setBasePath(getBasePath())
                    .setContentType(ContentType.URLENC).setConfig(RestAssured.config().encoderConfig(encoderConfig()
                            .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                    .build();
        }
        return REQ_SPEC_ENCODED;
    }
    public RequestSpecification getREQ_SPEC_FORM() {
        if (REQ_SPEC_FORM == null) {
            REQ_SPEC_FORM = new RequestSpecBuilder()
                    .addHeader("cookie", cookie)
                    .setBaseUri(BASE_URL)
                    .setBasePath(getBasePath())
                    .setContentType(ContentType.MULTIPART)
                    .build();
        }
        return REQ_SPEC_FORM;
    }

    public RequestSpecification getSPEC_ENCODED_ID(int id) {
            return REQ_SPEC_ENCODED_ID = new RequestSpecBuilder()
                    .addHeader("cookie", cookie)
                    .setBaseUri(BASE_URL)
                    .setBasePath(getBasePath() + id)
                    .setContentType(ContentType.URLENC).setConfig(RestAssured.config().encoderConfig(encoderConfig()
                            .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                    .build();
    }

    public RequestSpecification getSPEC_ENCODED_PATH(int id, String path, ContentType type) {
            return REQ_SPEC_ENCODED_PATH = new RequestSpecBuilder()
                    .addHeader("cookie", cookie)
                    .setBaseUri(BASE_URL)
                    .setBasePath(getBasePath() + id + "/" + path)
                    .setContentType(type).setConfig(RestAssured.config().encoderConfig(encoderConfig()
                            .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                    .build();
    }
    public RequestSpecification getSPEC_ENCODED_PATH(String path) {
        return REQ_SPEC_ENCODED_PATH = new RequestSpecBuilder()
                .addHeader("cookie", cookie)
                .setBaseUri(BASE_URL)
                .setBasePath(getBasePath() + "/" + path)
                .setContentType(ContentType.URLENC).setConfig(RestAssured.config().encoderConfig(encoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .build();
    }

    public RequestSpecification getSPEC_HTML() {
        if (REQ_SPEC_HTML == null) {
            REQ_SPEC_HTML = new RequestSpecBuilder()
                    .addHeader("cookie", cookie)
                    .setBaseUri(BASE_URL)
                    .setBasePath(getBasePath())
                    .setContentType(ContentType.HTML)
                    .build();
        }
        return REQ_SPEC_HTML;
    }

    public RequestSpecification getSPEC_ENCODED_ID_slash(int id) {
        return new RequestSpecBuilder()
                .addHeader("cookie", cookie)
                .setBaseUri(BASE_URL)
                .setBasePath(getBasePath() + "/" + id)
                .setContentType(ContentType.URLENC).setConfig(RestAssured.config().encoderConfig(encoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .build();
    }


    public String getCookie() {
        return cookie;
    }


    public RequestSpecification requestSpecification(String basePath) {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setBasePath(basePath)
                .setContentType(ContentType.MULTIPART)
                .build();
    }

    public ResponseSpecification responseSpecificationStatus(int status) {
        return new ResponseSpecBuilder()
                .expectStatusCode(status).build();
    }

    public void installSpecifications(ResponseSpecification response) {
//        RestAssured.requestSpecification = request;
        RestAssured.responseSpecification = response;
    }
}

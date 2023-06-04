package com.lista.automation.api.assert_response;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.util.function.Function;

public class RestResponse <T> {
    private Response response;
    private Function<Response, T> extractor;

    public RestResponse(Response response, Function<Response, T> extractor) {
        this.response = response;
        this.extractor = extractor;
    }

    public ValidatableResponse validate() {
        return response.then();
    }

    public T extract() {
        return extractor.apply(response);
    }

    public Response row() {
        return response;
    }
}

package com.lista.automation.api.assert_response;

import com.lista.automation.api.pojo.service.ServiceCreateRequest;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by Palchitsky Alex
 */
public abstract class VerifyResponse<SELF_TYPE extends VerifyResponse<?>>{

    protected SELF_TYPE selfType;
    protected Response response;
    protected SoftAssertions softAssertions;

    protected VerifyResponse(Class<SELF_TYPE>selfType, Response response) {
        this.selfType = selfType.cast(this);
        this.response = response;
        this.softAssertions = new SoftAssertions();
    }


    public SELF_TYPE statusCodeIs(int statusCode) {
        assertThat(response.getStatusCode()).describedAs("status code").isEqualTo(statusCode);
        return selfType;

    }
    public SELF_TYPE hasABody(ServiceCreateRequest service) {
        ServiceCreateRequest serviceResponse = response.then().extract().as(ServiceCreateRequest.class);
        softAssertions.assertThat(serviceResponse).isEqualTo(service);
        return selfType;
    }

    public SELF_TYPE matchesSchema(String filePath) {
        softAssertions.assertThat(response.then().body(matchesJsonSchemaInClasspath(filePath)))
                .describedAs("Schema validation")
                .getWritableAssertionInfo();
        return selfType;
    }
    public void assertAll() {
        softAssertions.assertAll();
    }
}


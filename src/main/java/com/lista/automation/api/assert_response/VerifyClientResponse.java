package com.lista.automation.api.assert_response;

import com.lista.automation.api.pojo.client.ClientGetResponse;
import io.restassured.response.Response;

/**
 * Created by Palchitsky Alex
 */
public class VerifyClientResponse extends VerifyResponse<VerifyClientResponse> {

    public VerifyClientResponse(Response response) {
        super(VerifyClientResponse.class, response);

    }

    public static VerifyClientResponse assertThat(Response response) {
        return new VerifyClientResponse(response);
    }

    public VerifyClientResponse postHasClient(ClientGetResponse expected) {
        ClientGetResponse clientResponse =
                response.then().extract().response().as(ClientGetResponse.class);

        softAssertions
                .assertThat(clientResponse)
                .describedAs("client")
                .isEqualTo(expected);

        return this;
    }
}

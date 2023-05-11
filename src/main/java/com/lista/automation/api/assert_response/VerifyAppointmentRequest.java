package com.lista.automation.api.assert_response;

import io.restassured.response.Response;

/**
 * Created by Palchitsky Alex
 */
public class VerifyAppointmentRequest extends VerifyResponse<VerifyAppointmentRequest> {

    public VerifyAppointmentRequest(Response response) {
        super(VerifyAppointmentRequest.class, response);

    }

    public static VerifyAppointmentRequest assertThat(Response response) {
        return new VerifyAppointmentRequest(response);
    }

}

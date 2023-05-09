package com.lista.automation.api.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthPojo {
    @JsonProperty("time_zone")
    private String timeZone;
    private String email;
    @JsonProperty("current-password")
    private String currentPassword;
}

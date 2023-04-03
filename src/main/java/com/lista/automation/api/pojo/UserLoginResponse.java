package com.lista.automation.api.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserLoginResponse {

    private String time_zone;
    private String email;
    @JsonProperty("current-password")
    private String currentPassword;
}

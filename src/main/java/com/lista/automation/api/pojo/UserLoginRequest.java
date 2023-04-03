package com.lista.automation.api.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@AllArgsConstructor
public class UserLoginRequest  {
    @JsonProperty("time_zone")
    private String timeZone;
    private String email;
    @JsonProperty("current-password")
    private String currentPassword;
}

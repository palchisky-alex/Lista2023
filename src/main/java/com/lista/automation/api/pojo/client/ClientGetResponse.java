package com.lista.automation.api.pojo.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

@Data
@NonNull
@JsonIgnoreProperties(ignoreUnknown=true)
public class ClientGetResponse {
    private String id;
    private String name;
    private String phone;
    @JsonProperty("profile_image")
    private String profileImage;
    private String address;
    private String status;
    @JsonProperty("permit_ads")
    private boolean permitAds;
    @JsonProperty("is_unsubscribed")
    private boolean isUnsubscribed;
    @JsonProperty("last_appointment")
    private Object lastAppointment;
    @JsonProperty("phone_canonical")
    private String phoneCanonical;

}



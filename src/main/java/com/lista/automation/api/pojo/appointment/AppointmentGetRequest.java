package com.lista.automation.api.pojo.appointment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Created by Palchitsky Alex
 */

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class AppointmentGetRequest {

        private String offTime;
        private String note;
        private String address;
        private String birthdate;
        private String totalPrice;
        private String addedDate;
        private boolean permitAds;
        private boolean isConfirmed;
        private String start;
        private boolean isUnsubscribed;
        private boolean isBookedRemotely;
        private String profilePicture;
        private List<ServicesList> services;
        @JsonProperty("client_id")
        private int clientId;
        private String thankingSmsId;
        private String reminderSmsId;
        private String phone;
        @JsonProperty("name")
        private String clientName;
        private String end;
        @JsonProperty("id")
        private int appointmentID;
        private String phoneCanonical;
        private boolean isRecurring;
        private String status;
        private String phone_canonical;


        @Data
        @JsonIgnoreProperties(ignoreUnknown=true)
        @NoArgsConstructor
        @ToString
        @EqualsAndHashCode
        public static class ServicesList{
            private int duration;
            private String color;
            private int price;
            private int count;
            @JsonProperty("name")
            private String serviceName;
            private int id;
        }
    }


package com.lista.automation.api.pojo.appointment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lista.automation.api.pojo.client.strategy.PhoneNumberStrategy;
import com.lista.automation.api.pojo.service.ServiceCreateRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uk.co.jemos.podam.common.PodamStrategyValue;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class AppointmentCreateRequest {

    private String start;
    private String end;
    @JsonProperty("id")
    private int appointmentID;
    @JsonProperty("client_id")
    private String clientID;
    @JsonProperty("name")
    private String clientName;

    @JsonProperty("worker_id")
    private int workerID;
    @JsonProperty("total_price")
    private int totalPrice;

    private List<ServiceCreateRequest> services;
    private String note;
    @JsonProperty("is_reminders_set")
    private boolean isRemindersSet;
    private String address;
    private int duration;
    @PodamStrategyValue(PhoneNumberStrategy.class)
    private String phone;
    private String status;
    @JsonProperty("is_unsubscribed")
    private String isUnsubscribed;
    private String off_time;
    private String birthdate;
    private String added_date;
    private String reminder_sms_id;
    private String thanking_sms_id;
    private String is_confirmed;
    private String is_booked_remotely;
    private String phone_canonical;
    private String is_recurring;
}

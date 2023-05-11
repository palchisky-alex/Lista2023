package com.lista.automation.api.pojo.appointment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class AppointmentDeleteResponse {
    @JsonProperty("is_notification_sent")
    private boolean isNotificationSent;
    @JsonProperty("is_sms_failed")
    private boolean isSmsFailed;
}

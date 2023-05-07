package com.lista.automation.api.pojo.general_settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class GeneralSettingsPojo {

    private String currency;
    private String lang;
    private String permission_level;
    private String business_logo;
    private String business_name;
    private String business_address;
    private String showCalendarFrom;
    private String showCalendarTo;
    private String calendarDefaultView;
    private String slotDuration;
}

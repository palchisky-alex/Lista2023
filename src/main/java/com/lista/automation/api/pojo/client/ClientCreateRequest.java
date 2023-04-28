package com.lista.automation.api.pojo.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lista.automation.api.pojo.client.strategy.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uk.co.jemos.podam.common.PodamStrategyValue;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ClientCreateRequest {

    private boolean is_filling_up_sent;
    private String status;
    @JsonProperty("permit_ads")
    private boolean permitAds;
    @PodamStrategyValue(BirthdateStrategy.class)
    private String birthdate;
    @PodamStrategyValue(BirthyearStrategy.class)
    private String birthyear;
    @PodamStrategyValue(AddressStrategy.class)
    private String address;
    @PodamStrategyValue(DebtsStrategy.class)
    private String debts;
    @PodamStrategyValue(PhoneNumberStrategy.class)
    private String phone;
    @PodamStrategyValue(MailStrategy.class)
    @EqualsAndHashCode.Include
    private String email;
    @PodamStrategyValue(NoteStrategy.class)
    private String notes;

    @PodamStrategyValue(NameStrategy.class)
    private String name;
    @PodamStrategyValue(GenderStrategy.class)
    private String gender;

    @Data
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class Debts {

        @PodamStrategyValue(DataTimeStrategy.class)
        public String date;
        public String desc;
        public int sum;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class Notes {
        @PodamStrategyValue(DataTimeStrategy.class)
        public String added;
        public String text;
    }

}

package com.lista.automation.api.pojo.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lista.automation.api.pojo.client.strategy.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uk.co.jemos.podam.common.PodamStrategyValue;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@NoArgsConstructor
@ToString
public class ClientCreateRequest {

    private boolean is_filling_up_sent;
    private String status;
    private boolean permit_ads;
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

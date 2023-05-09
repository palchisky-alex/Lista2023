package com.lista.automation.api.pojo.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lista.automation.api.pojo.client.strategy.DataTimeStrategy;
import com.lista.automation.api.pojo.service.strategy.DurationStrategy;
import com.lista.automation.api.pojo.service.strategy.LongNameStrategy;
import lombok.*;
import lombok.experimental.Accessors;
import uk.co.jemos.podam.common.PodamStrategyValue;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown=true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(setterPrefix = "set")
@AllArgsConstructor
public class ServiceCreateRequest {

    @JsonProperty("id")
    private String serviceID;

    @JsonProperty("name")
    @PodamStrategyValue(LongNameStrategy.class)
    private String serviceName;

    @JsonProperty("duration")
    @PodamStrategyValue(DurationStrategy.class)
    private int serviceDuration;

    @PodamStrategyValue(DurationStrategy.class)
    private int price;

    private String color;

    @JsonProperty("category_id")
    private int categoryID;

    @PodamStrategyValue(DataTimeStrategy.class)
    private String added;

    private CategoryRequestCreate category;

    @Data
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class CategoryRequestCreate {
        @JsonProperty("name")
        private String categoryName;
        private String id;
    }

//    public static ServiceCreateRequest getInstance() {
//        ServiceCreateRequest servicesBody = ServiceCreateRequest.builder().build();
//
//        return servicesBody;
//
//    }
}

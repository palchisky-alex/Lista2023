package com.lista.automation.api.pojo.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lista.automation.api.pojo.client.strategy.DataTimeStrategy;
import com.lista.automation.api.pojo.service.strategy.DurationStrategy;
import com.lista.automation.api.pojo.service.strategy.LongNameStrategy;
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
public class ServiceCreateRequest {

    private String id;
    @PodamStrategyValue(LongNameStrategy.class)
    @JsonProperty("name")
    private String serviceName;
    @PodamStrategyValue(DurationStrategy.class)
    @JsonProperty("duration")
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
        private String name;
        private String id;
    }
}

package com.lista.automation.api.pojo.service.strategy;

import net.datafaker.Faker;
import net.datafaker.providers.base.Name;
import net.datafaker.providers.base.Time;
import uk.co.jemos.podam.common.AttributeStrategy;

import java.lang.annotation.Annotation;
import java.time.LocalDate;
import java.util.List;

public class LongNameStrategy implements AttributeStrategy<String> {
    @Override
    public String getValue(Class<?> attrType, List<Annotation> attrAnnotations) {
        String phone = new Faker().phoneNumber().cellPhone();
        String fullName = new Faker().name().fullName();
        return fullName +"_"+phone+"_"+ LocalDate.now();
    }
}

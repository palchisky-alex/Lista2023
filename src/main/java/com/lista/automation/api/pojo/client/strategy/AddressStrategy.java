package com.lista.automation.api.pojo.client.strategy;

import net.datafaker.Faker;
import uk.co.jemos.podam.common.AttributeStrategy;

import java.lang.annotation.Annotation;
import java.util.List;

public class AddressStrategy implements AttributeStrategy<String> {
    @Override
    public String getValue(Class<?> aClass, List<Annotation> list) {
        return new Faker().address().fullAddress();
    }
}

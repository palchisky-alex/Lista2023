package com.lista.automation.api.pojo.client.strategy;

import net.datafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;
import uk.co.jemos.podam.common.AttributeStrategy;

import java.lang.annotation.Annotation;
import java.util.List;

public class PhoneNumberStrategy implements AttributeStrategy<String> {
    @Override
    public String getValue(Class<?> aClass, List<Annotation> list) {
        return "[\"" + new Faker().numerify("050#-##-##-##")+ "\"]";
    }
}

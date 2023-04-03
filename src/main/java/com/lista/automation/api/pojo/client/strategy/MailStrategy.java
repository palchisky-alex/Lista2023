package com.lista.automation.api.pojo.client.strategy;

import net.datafaker.Faker;
import uk.co.jemos.podam.common.AttributeStrategy;

import java.lang.annotation.Annotation;
import java.util.List;

public class MailStrategy implements AttributeStrategy<String> {
    @Override
    public String getValue(Class<?> aClass, List<Annotation> list) {
        Faker faker = new Faker();
        return faker.internet().emailAddress();
    }
}

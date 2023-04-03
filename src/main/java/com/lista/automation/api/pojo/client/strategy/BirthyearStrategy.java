package com.lista.automation.api.pojo.client.strategy;

import net.datafaker.Faker;
import uk.co.jemos.podam.common.AttributeStrategy;

import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BirthyearStrategy implements AttributeStrategy<String> {
    @Override
    public String getValue(Class<?> aClass, List<Annotation> list) {
        Date birthdate = new Faker().date().birthday();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(birthdate);
    }
}

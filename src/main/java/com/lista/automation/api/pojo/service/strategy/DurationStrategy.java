package com.lista.automation.api.pojo.service.strategy;

import uk.co.jemos.podam.common.AttributeStrategy;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DurationStrategy implements AttributeStrategy<Integer> {
    @Override
    public Integer getValue(Class<?> attrType, List<Annotation> attrAnnotations) {
        return  ThreadLocalRandom.current()
                .nextInt(10, 900);
    }
}

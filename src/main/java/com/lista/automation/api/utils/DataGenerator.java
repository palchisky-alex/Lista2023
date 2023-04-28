package com.lista.automation.api.utils;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class DataGenerator {
    public static <T> T getSimpleData(Class<T> clazz) {
        PodamFactory factory = new PodamFactoryImpl();
        return factory.manufacturePojo(clazz);
    }
}

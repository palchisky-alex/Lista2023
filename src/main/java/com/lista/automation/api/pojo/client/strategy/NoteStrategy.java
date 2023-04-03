package com.lista.automation.api.pojo.client.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lista.automation.api.pojo.client.ClientCreateRequest;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uk.co.jemos.podam.common.AttributeStrategy;

import java.lang.annotation.Annotation;
import java.util.List;

public class NoteStrategy implements AttributeStrategy<String> {
    @Override
    public String getValue(Class<?> aClass, List<Annotation> list) {
        PodamFactory factory = new PodamFactoryImpl();
        ClientCreateRequest.Notes status = factory.manufacturePojo(ClientCreateRequest.Notes.class);

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(status);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return String.format("[%s]", json);

    }
}

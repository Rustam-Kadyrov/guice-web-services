package com.rustam.project.config.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Provider;

/**
 * Created by Rustam Kadyrov on 25.06.2017.
 */
public class ObjectMapperProvider implements Provider<ObjectMapper> {
    @Override
    public ObjectMapper get() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}

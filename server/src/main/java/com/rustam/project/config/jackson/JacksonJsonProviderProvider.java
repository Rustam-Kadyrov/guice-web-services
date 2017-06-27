package com.rustam.project.config.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Created by Rustam Kadyrov on 25.06.2017.
 */
public class JacksonJsonProviderProvider implements Provider<JacksonJsonProvider> {
    private final ObjectMapper mapper;

    @Inject
    JacksonJsonProviderProvider(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public JacksonJsonProvider get() {
        return new JacksonJsonProvider(mapper);
    }
}

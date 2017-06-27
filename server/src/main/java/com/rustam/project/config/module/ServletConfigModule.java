package com.rustam.project.config.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.Scopes;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.ServletModule;
import com.rustam.project.config.EmbeddedContainer;
import com.rustam.project.config.jackson.JacksonJsonProviderProvider;
import com.rustam.project.config.jackson.ObjectMapperProvider;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * Created by Rustam Kadyrov on 25.06.2017.
 */
public class ServletConfigModule extends ServletModule {
    @Override
    protected void configureServlets() {
        binder().requireExplicitBindings();
        bind(EmbeddedContainer.class);

        //persist support
        install(new JpaPersistModule("myJpaUnit"));
        bind(PersistFilter.class);

        //injection into servlets, mappings
        bind(GuiceFilter.class);
        bind(GuiceContainer.class);

        //jackson auto serialize/deserialize every request, tuned objectMapper
        bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class).in(Scopes.SINGLETON);
        bind(JacksonJsonProvider.class).toProvider(JacksonJsonProviderProvider.class).in(Scopes.SINGLETON);

        //session-per-http-request
        filter("/*").through(PersistFilter.class);

        serve("/*").with(GuiceContainer.class);
    }

}

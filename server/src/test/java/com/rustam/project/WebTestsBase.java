package com.rustam.project;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.rustam.project.config.jackson.ObjectMapperProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.testng.annotations.BeforeClass;

import javax.ws.rs.core.MediaType;

/**
 * Created by Rustam Kadyrov on 06.07.2017.
 */
public class WebTestsBase extends TestsBase {
    private static final String REST_URL = "http://localhost:8081/my-app";
    private Client client;

    @BeforeClass
    public void beforeClass() throws Exception {
        super.beforeClass();
        ClientConfig clientConfig = new DefaultClientConfig();
        JacksonJaxbJsonProvider jacksonProvider = new JacksonJaxbJsonProvider();
        jacksonProvider.setMapper(new ObjectMapperProvider().get());
        clientConfig.getSingletons().add(jacksonProvider);
        client = Client.create(clientConfig);
    }

    protected <T> T invokePost(String methodUrl, Object request, GenericType<T> type) {
        WebResource resource = client.resource(REST_URL + methodUrl);
        return resource.type(MediaType.APPLICATION_JSON)
                .post(type, request);
    }

    protected <T> T invokePut(String methodUrl, Object request, GenericType<T> type) {
        WebResource resource = client.resource(REST_URL + methodUrl);
        return resource.type(MediaType.APPLICATION_JSON)
                .put(type, request);
    }

    protected <T> T invokeDelete(String methodUrl, Object request, GenericType<T> type) {
        WebResource resource = client.resource(REST_URL + methodUrl);
        return resource.type(MediaType.APPLICATION_JSON)
                .delete(type, request);
    }

    protected <T> T invokeGet(String methodUrl, GenericType<T> type) {
        WebResource resource = client.resource(REST_URL + methodUrl);
        return resource.type(MediaType.APPLICATION_JSON)
                .get(type);
    }
}

package com.rustam.project;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.rustam.project.config.EmbeddedContainer;
import com.rustam.project.config.module.ApplicationModule;
import com.rustam.project.config.module.PropertiesModule;
import com.rustam.project.config.module.ServletConfigModule;

/**
 * Created by Rustam_Kadyrov on 25.06.2017.
 */
public class Application {

    private EmbeddedContainer embeddedContainer;

    public static void main(String[] args) throws Exception {
        new Application().start();
    }

    public void start() throws Exception {
        Injector injector = Guice.createInjector(
                new PropertiesModule("/my-app.properties"),
                new ServletConfigModule(),
                new ApplicationModule());

        embeddedContainer = injector.getInstance(EmbeddedContainer.class);
        embeddedContainer.start(injector);
    }

    public void stop() throws Exception {
        if (embeddedContainer != null) {
            embeddedContainer.stop();
        }
    }

}

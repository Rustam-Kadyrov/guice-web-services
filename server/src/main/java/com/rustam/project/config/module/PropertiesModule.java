package com.rustam.project.config.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Rustam_Kadyrov on 25.06.2017.
 */
public class PropertiesModule extends AbstractModule {

    private String propertiesFileName;

    public PropertiesModule(String propertiesFileName) {
        this.propertiesFileName = propertiesFileName;
    }

    @Override
    protected void configure() {
        try {
            Properties props = new Properties();
            InputStream propertiesFile = PropertiesModule.class.getResourceAsStream(propertiesFileName);
            if (propertiesFile == null) {
                throw new IllegalStateException("File not found in classpath: " + propertiesFileName);
            }
            props.load(propertiesFile);
            Names.bindProperties(binder(), props);
        } catch (IOException e) {
            throw new IllegalStateException("Can't load properties", e);
        }
    }
}

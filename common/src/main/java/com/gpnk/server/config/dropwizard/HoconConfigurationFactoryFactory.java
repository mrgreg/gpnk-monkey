package com.gpnk.server.config.dropwizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.ConfigurationFactoryFactory;

import javax.validation.Validator;

/**
 * Instantiates an instance of {@code HoconConfigurationFactory}
 * for use as a {@code ConfigurationFactory} in DropWizard.
 */
public class HoconConfigurationFactoryFactory<T> implements ConfigurationFactoryFactory<T> {

    @Override
    public ConfigurationFactory<T> create(Class<T> klass, Validator validator, ObjectMapper objectMapper,
                                          String propertyPrefix) {
        return new HoconConfigurationFactory<T>(klass, validator, objectMapper);
    }
}

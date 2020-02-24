package com.gpnk.server.config.dropwizard;

import com.gpnk.config.ConfigUtil;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.jasonclawson.jackson.dataformat.hocon.HoconFactory;
import com.typesafe.config.ConfigObject;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.ConfigurationSourceProvider;
import io.dropwizard.configuration.ConfigurationValidationException;

import java.io.IOException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

/**
 * A {@code ConfigurationFactory} subclass which parses DropWizard config from HOCON
 * {@code application.conf} file instead of a conventional yml file.
 */
public class HoconConfigurationFactory<T> implements ConfigurationFactory<T> {

    private final Class<T> klass;
    private final Validator validator;
    private final ObjectMapper mapper;
    private final HoconFactory hoconFactory;

    /**
     * A standard {@code ConfigurationFactory} constructor. Here we also disable failure
     * on unknown properties in the config file.
     */
    public HoconConfigurationFactory(final Class<T> klass, final Validator validator, final ObjectMapper mapper) {
        this.klass = klass;
        this.validator = validator;
        this.mapper = mapper.copy();
        this.hoconFactory = new HoconFactory();

        this.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /** {@inheritDoc} */
    @Override
    public T build(ConfigurationSourceProvider provider, String path) throws IOException, ConfigurationException {
        return build();
    }

    /** {@inheritDoc} */
    @Override
    public T build() throws IOException, ConfigurationException {
        ConfigObject configTree = ConfigUtil.load().root();
        JsonNode jsonConfig = mapper.readTree(hoconFactory.createParser(configTree.render()));
        T config = mapper.readValue(new TreeTraversingParser(jsonConfig), klass);

        Set<ConstraintViolation<T>> errors = validator.validate(config);
        if (!errors.isEmpty()) {
            // ConfigurationValidationException(String path, Set<ConstraintViolation<T>> errors)
            // in our case, the path is empty but we do have errors
            throw new ConfigurationValidationException("", errors);
        }
        return config;
    }
}

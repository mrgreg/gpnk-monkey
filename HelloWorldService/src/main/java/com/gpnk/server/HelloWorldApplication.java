package com.gpnk.server;

import com.gpnk.helloworld.HelloWorldResource;
import com.gpnk.helloworld.PingHealthCheck;
import com.gpnk.models.HelloWorldConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.logging.LoggingFeature;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Our sample Application class.
 */
public class HelloWorldApplication extends Application<HelloWorldConfiguration> {

    // Created using this as a reference: https://www.dropwizard.io/1.3.13/docs/getting-started.html

    // Code based TODOs:
    // TODO: DI
    // TODO: logging
    // TODO: testing frameworks (unit and integration against docker images)
    // TODO: db connectivity / DAO patterns
    // TODO: flyway
    // TODO: jooq (worth looking into JDBI?)
    // TODO: gRPC (both server side and generate client modules)
    // TODO: version resource (on admin port?)
    // TODO: swagger doc generation and hosting
    // TODO: make Configuration generic, but also allow per application settings


    // Build based TODOs:
    // TODO: build assembly (docker image, not shaded jar?)
    // TODO: code static analysis tools (checkstyle, etc.)
    // TODO: build static analysis tools (dependency version checking, etc.)

    /**
     * Main method for the Application.
     * @param args - Application's arguments
     * @throws Exception - Throws Exception from the Application.
     */
    public static void main(final String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }

    /**
     * @return Name of the app.
     */
    @Override
    public String getName() {
        return "hello-world";
    }

    /**
     * Initialize the app.
     * @param bootstrap - DropWizard's bootstrap class.
     */
    @Override
    public void initialize(final Bootstrap<HelloWorldConfiguration> bootstrap) {
        // nothing yet
    }

    /**
     * Configures the app with resources, healthchecks, logging, etc.
     * @param configuration - config from hello-world.yml
     * @param environment - Dropwizard's environment
     */
    @Override
    public void run(final HelloWorldConfiguration configuration, final Environment environment) throws Exception {

        // TODO: use DI to bind and register application specific resources
        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(resource);

        // TODO: use DI to bind and register application specific HealthChecks
        environment.healthChecks().register("ping", new PingHealthCheck());

        // Enable logging of REST requests and responses on the server
        // TODO: enable logging in a similar way on the REST client
        // Verbosity determines how detailed the logged message is, currently logs headers and text (which is everything);
        // maxEntitySize - maximum number of entity bytes to log and buffer, will print and buffer up to the specified
        // number of bytes cutting off the rest.
        // For more details: https://www.javaguides.net/2018/06/jersey-rest-logging-using-loggingfeature.html
        environment.jersey().register(new LoggingFeature(Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME),
                Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 10000));

    }
}

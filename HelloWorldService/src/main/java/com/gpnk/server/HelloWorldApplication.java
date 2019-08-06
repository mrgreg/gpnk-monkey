package com.gpnk.server;

import com.gpnk.helloworld.HelloWorldResource;
import com.gpnk.helloworld.PingHealthCheck;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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


    public static void main(String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        // nothing yet
    }

    @Override
    public void run(HelloWorldConfiguration configuration, Environment environment) throws Exception {

        // TODO: use DI to bind and register application specific resources
        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(resource);

        // TODO: use DI to bind and register application specific HealthChecks
        environment.healthChecks().register("ping", new PingHealthCheck());

    }
}

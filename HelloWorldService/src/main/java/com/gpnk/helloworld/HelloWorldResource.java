package com.gpnk.helloworld;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.gpnk.common.Resource;
import org.slf4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

/**
 * Our sample Resource.
 */
@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource implements Resource {

    @Inject
    private Logger log;

    @Inject
    @Named("template")
    private String template;

    @Inject
    @Named("defaultName")
    private String defaultName;

    /** Default constructor for injection. */
    public HelloWorldResource() {
    }

    /**
     * Sample GET method.
     * @param name - Says "Hello, Name", defaults to Stranger
     * @return - String "Hello, Name"
     */
    @GET
    @Timed
    public String sayHello(@QueryParam("name") final Optional<String> name) {
        performSampleLogging("Get method /sayHello called. Using name = " + name.orElse(defaultName));
        final String value = String.format(template, name.orElse(defaultName));
        return value;
    }

    /**
     * Demonstrates logging. As per logback config in hello.yml, messages at DEBUG
     * level and higher are printed to console appender.
     * @param message - message to print to logs.
     */
    private void performSampleLogging(final String message) {
        log.trace(message);
        log.debug(message);
        log.info(message);
        log.warn(message);
        log.error(message);
    }


}

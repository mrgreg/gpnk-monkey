package com.gpnk.helloworld;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.gpnk.common.Resource;
import com.gpnk.models.User;
import com.gpnk.models.WeatherReport;
import org.slf4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
    @Named("nameOnlyTemplate")
    private String nameOnlyTemplate;

    @Inject
    @Named("weatherTemplate")
    private String weatherTemplate;

    @Inject
    @Named("defaultName")
    private String defaultName;

    @Inject
    private HelloWorldService helloWorldService;

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

        if (name.isPresent()) {
            log.debug("hello called with user {}", name.get());
            Optional<WeatherReport> weatherForUser = helloWorldService.getWeatherForUser(name.get());
            if (weatherForUser.isPresent()) {
                return String.format(weatherTemplate, name.get(), weatherForUser.get().toString());
            }
        }
        return String.format(nameOnlyTemplate, name.orElse(defaultName));
    }

    /**
     * Sample PUT method.
     * Creates a user.
     */
    @PUT
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed
    public void createUser(final User user) {
        helloWorldService.createUser(user);
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

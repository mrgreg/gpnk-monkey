package com.gpnk.helloworld;

import com.gpnk.common.Resource;
import com.gpnk.models.User;
import com.gpnk.models.WeatherReport;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;

import java.util.Optional;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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
    @SuppressWarnings("checkstyle:OperatorWrap")
    @GET
    @Timed
    @Operation(description = "Says hello with the name, if the name is given. Outputs weather forecast for registered users.")
    public String sayHello(@Parameter(description = "Name of the user", required = false)
                           @QueryParam("name") final Optional<String> name) {

        if (name.isPresent()) {
            log.debug("hello called with user {}", name.get());
            String nameStr = name.get().trim();
            // demonstrates a WebApplicationException with response code = 400
            if (!isOnlyLetters(nameStr)) {
                throw new BadRequestException("Invalid name '" + nameStr + "'. A name should only contain characters.");
            }

            Optional<WeatherReport> weatherForUser = helloWorldService.getWeatherForUser(nameStr);
            if (weatherForUser.isPresent()) {
                return String.format(weatherTemplate, nameStr, weatherForUser.get().toString());
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
    @Operation(description = "Creates a user.")
    public void createUser(@Parameter(description = "Information about the user: name and zipcode.", required = true)
                           final User user) {
        helloWorldService.createUser(user);
    }

    private boolean isOnlyLetters(String s) {
        return s.matches("[a-zA-Z]+");
    }
}

package com.gpnk.helloworld;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

    private final Logger log = LoggerFactory.getLogger(HelloWorldResource.class);

    private final String template;
    private final String defaultName;

    public HelloWorldResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
    }

    @GET
    @Timed
    public String sayHello(@QueryParam("name") Optional<String> name) {
        performSampleLogging("Get method /sayHello called. Using name = " + name.orElse(defaultName));
        final String value = String.format(template, name.orElse(defaultName));
        return value;
    }

    /* Demonstrates logging. As per logback config in hello.yml, messages at DEBUG
     * level and higher are printed to console appender.
     **/
    private void performSampleLogging(String message) {
        log.trace(message);
        log.debug(message);
        log.info(message);
        log.warn(message);
        log.error(message);
    }


}

package com.gpnk.common;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Just a simple ping endpoint.
 */
@Path("/ping")
@Produces(MediaType.TEXT_PLAIN)
public class PingResource implements Resource {

    /**
     * ping -> pong
     */
    @GET
    @Timed
    public String ping() {
        return "pong";
    }
}

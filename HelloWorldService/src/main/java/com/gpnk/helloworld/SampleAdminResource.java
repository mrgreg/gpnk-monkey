package com.gpnk.helloworld;

import com.gpnk.common.AdminResource;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/sample")
@Produces(MediaType.APPLICATION_JSON)
public class SampleAdminResource implements AdminResource {

    /**
     * Sample Admin Resource GET endpoint.
     * @return - A JSON hello response.
     */
    @GET
    @Timed
    public String sayHello() {
        return "{ \"msg\": \"Hello, there! I am a custom admin endpoint.\"}";
    }

}

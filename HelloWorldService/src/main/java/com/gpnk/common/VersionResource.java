package com.gpnk.common;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URL;

/**
 * Contains an endpoint that reports the current version of the code running.
 * TODO: move this to an admin resource once we figure out how to register them
 */
@Path("/version")
@Produces(MediaType.TEXT_PLAIN)
public class VersionResource implements Resource {

    /**
     * Gets the version of the running code
     */
    @GET
    public String getVersion() throws IOException {

        URL url = Resources.getResource("git.properties");
        return Resources.toString(url, Charsets.UTF_8);
    }
}

package com.gpnk.common.response.examples;

import com.gpnk.common.response.ExceptionResponseHelper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Demonstrates a custom mapper with a custom status code.
 */
@Provider
public class CustomResponseCodeExceptionMapper implements ExceptionMapper<CustomResponseCodeException> {

    /** {@inheritDoc} */
    @Override
    public Response toResponse(CustomResponseCodeException e) {
        // use a made-up status code
        ExceptionResponseHelper helper = new ExceptionResponseHelper(510);
        return helper.toResponse(e);
    }
}

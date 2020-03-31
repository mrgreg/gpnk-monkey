package com.gpnk.common.response;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Default Exception Mapper uses HTTP code 500.
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

    /** {@inheritDoc} */
    @Override
    public Response toResponse(Exception e) {
        ExceptionResponseHelper helper = new ExceptionResponseHelper(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        return helper.toResponse(e);
    }
}

package com.gpnk.common.response.examples;

import com.gpnk.common.response.ExceptionResponseHelper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Demonstrates a fully customized ExceptionMapper.
 * Any ExceptionMapper needs to log the Exception before building a Response.
 */
@Provider
public class FullyCustomizedExceptionMapper implements ExceptionMapper<FullyCustomizedSampleException> {

    /** {@inheritDoc} */
    @Override
    public Response toResponse(FullyCustomizedSampleException e) {
        ExceptionResponseHelper helper = new ExceptionResponseHelper(-1);
        long loggedExceptionId = helper.log(e);
        SampleEntity response = new SampleEntity(String.format("%016x", loggedExceptionId), "Custom HTTP Code 555", e.getLocalizedMessage());

        return Response.status(555)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(response)
                .build();
    }
}

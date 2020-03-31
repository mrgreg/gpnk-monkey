package com.gpnk.common.response;

import io.dropwizard.jersey.errors.ErrorMessage;
import io.dropwizard.jersey.errors.LoggingExceptionMapper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Helper class to re-use the functionality of DropWizard's LoggingExceptionMapper,
 * https://github.com/dropwizard/dropwizard/blob/master/dropwizard-jersey/src/main/java/io/dropwizard/jersey/errors/LoggingExceptionMapper.java
 *
 * <p>To be used as a helper in custom-built Exception mappers, where appropriate.
 */
// Note: the approach of using inheritance to re-use LoggingExceptionMapper does not seem to work for creating specialized
// Exception mappers. Possibly the implementation is not quite right?
// Tried creating the base Exception mapper class:
// public abstract class BaseExceptionMapper<E extends Exception> extends LoggingExceptionMapper<Exception> { ... }
// Generic Exception mapper:
// public class GenericExceptionMapper extends BaseExceptionMapper<Exception> { ... }
// The above works as expected, but the following specialized Exception mapper is not being picked:
// public class WeatherServiceExceptionMapper extends BaseExceptionMapper<WeatherServiceException> { ... }
// Contrary to the expected behavior, WeatherServiceException is picked up by the GenericExceptionMapper.
public class ExceptionResponseHelper extends LoggingExceptionMapper<Exception> {

    private final int statusCode;

    /**
     * @param statusCode - HTTP status code to use in Response
     */
    public ExceptionResponseHelper(final int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Include Exception's message in the <code>details</code> field of the Response.
     * @param exception - Mapped Exception.
     * @return - Response object ready to be returned to the caller.
     */
    @Override
    public Response toResponse(Exception exception) {
        Response response = super.toResponse(exception);
        // rely on the behavior of LoggingExceptionMapper here
        if (exception instanceof WebApplicationException) {
            return response;
        }

        ErrorMessage currentMessage = (ErrorMessage) response.getEntity();
        ErrorMessage detailedMessage = new ErrorMessage(statusCode, currentMessage.getMessage(),
                exception.getLocalizedMessage());

        return Response.status(statusCode)
                .type(response.getMediaType())
                .entity(detailedMessage)
                .build();

    }

    /**
     * Delegates to LoggingExceptionMapper to log the given Exception.
     * @return - the ID of the logged message.
     */
    public long log(Exception e) {
        return super.logException(e);
    }
}

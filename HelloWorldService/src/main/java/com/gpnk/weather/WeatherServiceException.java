package com.gpnk.weather;

/**
 * Indicates a non-200 response from Dark Sky Api.
 */
public class WeatherServiceException extends RuntimeException {

    private static final long serialVersionUID = 20200416141456L;

    /**
     * @param message - Error message
     */
    public WeatherServiceException(final String message) {
        super(message);
    }
}

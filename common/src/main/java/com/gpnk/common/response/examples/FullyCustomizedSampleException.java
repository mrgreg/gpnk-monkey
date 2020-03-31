package com.gpnk.common.response.examples;

/**
 * Exists for the purpose of demonstrating a fully customized ExceptionMapper.
 */
public class FullyCustomizedSampleException extends RuntimeException {

    private static final long serialVersionUID = 20200416140408L;

    /**
     * @param message - Error message
     */
    public FullyCustomizedSampleException(final String message) {
        super(message);
    }
}

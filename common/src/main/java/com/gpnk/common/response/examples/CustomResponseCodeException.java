package com.gpnk.common.response.examples;

/**
 * Exists for the purpose of demonstrating a fully customized ExceptionMapper.
 */
public class CustomResponseCodeException extends RuntimeException {

    private static final long serialVersionUID = 20200416140123L;

    /**
     * @param message - Error message
     */
    public CustomResponseCodeException(final String message) {
        super(message);
    }
}

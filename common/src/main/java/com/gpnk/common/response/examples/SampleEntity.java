package com.gpnk.common.response.examples;

import lombok.Getter;

/**
 * Example entity returned in a fully customized Exception response.
 */
public class SampleEntity {

    @Getter
    private String exceptionId;

    @Getter
    private String field1;

    @Getter
    private String field2;

    /**
     * @param exceptionId - Logged Exception message id.
     * @param field1 - first sample field
     * @param field2 - second sample field
     */
    public SampleEntity(final String exceptionId, final String field1, final String field2) {
        this.exceptionId = exceptionId;
        this.field1 = field1;
        this.field2 = field2;
    }

}

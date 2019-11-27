package com.gpnk.common;

import com.codahale.metrics.health.HealthCheck;

/**
 * Ping HealthCheck that always returns healthy.
 */
public class PingHealthCheck extends HealthCheck {

    /**
     * Always returns healthy.
     */
    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}

package com.gpnk.helloworld;

import com.codahale.metrics.health.HealthCheck;

/**
 * Application's HealthCheck.
 */
public class PingHealthCheck extends HealthCheck {

    /**
     * Main healthcheck.
     * @return Result of the health check.
     * @throws Exception Propagates errors up.
     */
    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}

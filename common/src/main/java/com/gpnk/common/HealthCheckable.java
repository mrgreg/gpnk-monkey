package com.gpnk.common;

import com.codahale.metrics.health.HealthCheck;

/**
 * Codahale HealthCheck, sadly, uses an abstract class instead of a more flexible interface.  This interface is our
 * work-around to that design flaw.
 */
public interface HealthCheckable {

    /**
     * Gets the health of this component
     */
    HealthCheck.Result getHealth();
}

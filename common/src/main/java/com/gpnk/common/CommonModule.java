package com.gpnk.common;

/**
 * Guice bindings we expect all services will need.
 */
public class CommonModule extends GPNKModule {

    @Override
    protected void config() {
        bindResource(PingResource.class);
        bindHealthCheckable(PingResource.class);
    }
}

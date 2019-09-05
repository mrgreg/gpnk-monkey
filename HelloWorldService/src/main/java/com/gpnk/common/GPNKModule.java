package com.gpnk.common;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * A base Guice module class.  Modules that extend GPNKModule have the benefit of being able to easily bind in
 * Resource, AdminResource, and HealthCheck classes.
 *
 * <p>Modules that directly extend AbstractModule will still work, but will need to bind the above types manually.
 */
public abstract class GPNKModule extends AbstractModule {

    private Multibinder<Resource> resourceBinder;
    private Multibinder<AdminResource> adminResourceBinder;
    private Multibinder<HealthCheck> healthCheckBinder;

    @Override
    protected final void configure() {
        resourceBinder = Multibinder.newSetBinder(binder(), Resource.class);
        adminResourceBinder = Multibinder.newSetBinder(binder(), AdminResource.class);
        healthCheckBinder = Multibinder.newSetBinder(binder(), HealthCheck.class);
        config();
    }

    /** Configures a Binder via the exposed methods. */
    protected abstract void config();

    /**
     * Binds a Resource so it will get registered with DropWizard
     */
    protected void bindResource(final Class<? extends Resource> resourceClass) {
        resourceBinder.addBinding().to(resourceClass);
    }

    /**
     * Binds an AdminResource so it will get registered with DropWizard
     * TODO: Currently doesn't work
     */
    protected void bindAdminResource(final Class<? extends AdminResource> resourceClass) {
        adminResourceBinder.addBinding().to(resourceClass);
    }

    /**
     * Binds a HealthCheck so it will get registered with DropWizard
     */
    protected void bindHealthCheck(final Class<? extends HealthCheck> healthCheckClass) {
        healthCheckBinder.addBinding().to(healthCheckClass);
    }
}

package com.gpnk.server;

import com.gpnk.common.ApplicationStartupHook;
import com.gpnk.common.CommonModule;
import com.gpnk.common.HealthCheckable;
import com.gpnk.common.Resource;
import com.gpnk.models.HelloWorldConfiguration;
import com.gpnk.server.config.dropwizard.HoconConfigurationFactoryFactory;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.tavianator.sangria.slf4j.SangriaSlf4jModule;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.logging.LoggingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 * Our sample Application class.
 * Created using this as a reference: https://www.dropwizard.io/1.3.13/docs/getting-started.html
 */
public class HelloWorldApplication extends Application<HelloWorldConfiguration> {

    // the one logger we can't get via injection
    private final Logger log = LoggerFactory.getLogger(HelloWorldApplication.class);

    /**
     * Main method for the Application.
     * @param args - Application's arguments
     * @throws Exception - Throws Exception from the Application.
     */
    public static void main(final String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }

    /**
     * @return Name of the app.
     */
    @Override
    public String getName() {
        return "hello-world";
    }

    /**
     * Initialize the app.
     * Uses TypeSafe config for loading DropWizard's config by setting the
     * {@code ConfigurationFactoryFactory} to {@code HoconConfigurationFactoryFactory}.
     * @param bootstrap - DropWizard's bootstrap class.
     */
    @Override
    public void initialize(final Bootstrap<HelloWorldConfiguration> bootstrap) {
        bootstrap.setConfigurationFactoryFactory(new HoconConfigurationFactoryFactory<>());
    }

    /**
     * Configures the app with resources, healthchecks, logging, etc.
     * @param configuration - config from hello-world.yml
     * @param environment - Dropwizard's environment
     */
    @SuppressWarnings({"PMD.AvoidPrintStackTrace", "PMD.DoNotCallSystemExit"})
    @Override
    public void run(final HelloWorldConfiguration configuration, final Environment environment) throws Exception {

        // Build up our guice modules
        List<com.google.inject.Module> modules = new LinkedList<>();

        // default modules
        modules.add(new CommonModule());
        modules.add(new SangriaSlf4jModule());
        // configured modules
        List<String> moduleClassNames = configuration.getModules();
        moduleClassNames.forEach(moduleName -> {
            try {
                Module configuredModule = (Module) Class.forName(moduleName).getDeclaredConstructor().newInstance();
                modules.add(configuredModule);
                log.info("Adding module: " + moduleName);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Error adding module: " + moduleName, e);
                System.exit(1);
            }
        });

        // fire up the injector
        Injector injector = Guice.createInjector(modules);

        // get all of the rest resource classes and register them with jersey
        Set<Resource> resources = injector.getInstance(Key.get(new TypeLiteral<Set<Resource>>() { }));
        resources.forEach(resource ->  {
            environment.jersey().register(resource);
            log.info("Registering Resource: " + resource.getClass().getName());
        });

        // get all of the rest admin resource classes and register them with jersey
//        Set<AdminResource> adminResources = injector.getInstance(Key.get(new TypeLiteral<Set<AdminResource>>() { }));
//        adminResources.forEach(resource ->  {
//            // TODO: figure out how to actually do this
//            environment.jersey().register(resource);
//            log.info("Registering Admin Resource: " + resource.getClass().getName());
//        });

        // get all the bound HealthCheckables and register them
        Set<HealthCheckable> healthCheckables = injector.getInstance(Key.get(new TypeLiteral<Set<HealthCheckable>>() { }));
        healthCheckables.forEach(healthCheckable -> {
            String healthCheckName = healthCheckable.getClass().getSimpleName();
            environment.healthChecks().register(healthCheckName, new HealthCheckableBridge(healthCheckable));
            log.info("Registering HealthCheckable: " + healthCheckName);
        });

        // Enable logging of REST requests and responses on the server
        // TODO: enable logging in a similar way on the REST client
        // Verbosity determines how detailed the logged message is, currently logs headers and text (which is everything);
        // maxEntitySize - maximum number of entity bytes to log and buffer, will print and buffer up to the specified
        // number of bytes cutting off the rest.
        // For more details: https://www.javaguides.net/2018/06/jersey-rest-logging-using-loggingfeature.html
        environment.jersey().register(new LoggingFeature(java.util.logging.Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME),
                Level.INFO, LoggingFeature.Verbosity.HEADERS_ONLY, 10000));

        // get all the bound ApplicationStartupHooks, and run them
        Set<ApplicationStartupHook> hooks = injector.getInstance(Key.get(new TypeLiteral<Set<ApplicationStartupHook>>() { }));
        hooks.forEach(hook ->  {
            log.info("Running startup hook: " + hook.getClass().getName());
            hook.onStartup();
        });

    }

    /**
     * Used to bridge our HealthCheckable interface and codahale's HealthCheck abstract class
     */
    static class HealthCheckableBridge extends HealthCheck {

        private final HealthCheckable component;

        private HealthCheckableBridge(final HealthCheckable component) {
            this.component = component;
        }

        @Override
        protected Result check() throws Exception {
            return component.getHealth();
        }
    }
}

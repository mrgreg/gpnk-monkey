package com.gpnk.server;

import com.gpnk.common.AdminResource;
import com.gpnk.common.ApplicationStartupHook;
import com.gpnk.common.CommonModule;
import com.gpnk.common.HealthCheckable;
import com.gpnk.common.MetricsModule;
import com.gpnk.common.Resource;
import com.gpnk.common.response.GenericExceptionMapper;
import com.gpnk.common.response.examples.CustomResponseCodeExceptionMapper;
import com.gpnk.common.response.examples.FullyCustomizedExceptionMapper;
import com.gpnk.models.HelloWorldConfiguration;
import com.gpnk.server.config.dropwizard.HoconConfigurationFactoryFactory;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Charsets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.tavianator.sangria.slf4j.SangriaSlf4jModule;
import io.dropwizard.Application;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.jersey.jackson.JacksonMessageBodyProvider;
import io.dropwizard.jersey.setup.JerseyContainerHolder;
import io.dropwizard.servlets.assets.AssetServlet;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.v3.jaxrs2.integration.OpenApiServlet;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.servlet.ServletRegistration;

/**
 * Our sample Application class.
 * Created using this as a reference: https://www.dropwizard.io/1.3.13/docs/getting-started.html
 */
// TODO: Move this to common module and make it not specific to HelloWorld example
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

        // TODO: needs to be a singleton shared within the application; how to best enforce? To be implemented if/when
        // we customize Jackson object serialization/deserialization.
        // this Jackson config can be used to customize serialization/deserialization via the object mapper
        // this singleton object should be used by all entities (resources, servlets, etc.) that need Jackson customization
        final JacksonMessageBodyProvider jacksonMessageBodyProvider =
                new JacksonMessageBodyProvider(Jackson.newObjectMapper());

        // Build up our guice modules
        List<com.google.inject.Module> modules = new LinkedList<>();

        // default modules
        modules.add(new CommonModule());
        modules.add(new SangriaSlf4jModule());
        //configure metrics implementation with DropWizard's metric registry
        modules.add(new MetricsModule(environment.metrics()));

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
        Set<AdminResource> adminResources = injector.getInstance(Key.get(new TypeLiteral<Set<AdminResource>>() { }));
        registerAdminResources(adminResources, environment, jacksonMessageBodyProvider);

        // get all the bound HealthCheckables and register them
        Set<HealthCheckable> healthCheckables = injector.getInstance(Key.get(new TypeLiteral<Set<HealthCheckable>>() { }));
        healthCheckables.forEach(healthCheckable -> {
            String healthCheckName = healthCheckable.getClass().getSimpleName();
            environment.healthChecks().register(healthCheckName, new HealthCheckableBridge(healthCheckable));
            log.info("Registering HealthCheckable: " + healthCheckName);
        });

        registerSwaggerAtAdminPath(environment);

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

        // register Exception Mappers
        environment.jersey().register(new CustomResponseCodeExceptionMapper());
        environment.jersey().register(new GenericExceptionMapper());
        environment.jersey().register(new FullyCustomizedExceptionMapper());

    }

    /**
     * Insights into this implementation are in the following article:
     * https://spin.atomicobject.com/2015/03/30/jersey-servlets-dropwizard/
     *
     * <p>To register our custom admin resources on Jersey's admin path, we first need to register
     * a custom servlet in Jersey's admin environment that will then serve up the admin resources endpoints.
     *
     * <p>Note that the mapping for the custom servlet then becomes a part of an admin resource's endpoint.
     * For example, if we map the custom servlet to "/custom/*" in the admin environment and have an admin resource
     * with a "/sample" path, the admin resource endpoint is: http://localhost:8080/admin/custom/sample
     *
     * @param adminResources - Set of Admin Resources to register with custom servlet
     * @param environment - Jersey's environment
     * @param jacksonMessageBodyProvider - Jackson's serialization/deserialization settings
     */
    private void registerAdminResources(final Set<AdminResource> adminResources, final Environment environment,
                                        final JacksonMessageBodyProvider jacksonMessageBodyProvider) {

        // create new Jersey servlet
        DropwizardResourceConfig adminConfig = new DropwizardResourceConfig(environment.metrics());
        JerseyContainerHolder servletContainerHolder = new JerseyContainerHolder(new ServletContainer(adminConfig));

        // Add the servlet to the dropwizard environment and map it
        // Custom Admin resources will be at the path: http://localhost:8080/admin/custom/<resource_path>
        environment.admin().addServlet("Custom Admin Servlet", servletContainerHolder.getContainer()).addMapping("/custom/*");

        // Enable Jackson support
        adminConfig.register(jacksonMessageBodyProvider);

        // register Admin Resources as endpoints on the servlet
        adminResources.forEach(adminResource -> {
            adminConfig.register(adminResource);
            log.info("Registering Admin Resource: " + adminResource.getClass().getName());
        });

    }

    /**
     * Steps to register Swagger UI at admin path:
     * - Copy the contents of dist/ folder from Swagger UI distribution to resources/swagger
     * - Register Open API Servlet at admin path
     * - Register Swagger UI as an AssetServlet to serve static content from resources/swagger at admin path
     * - Adjust relative paths in swagger/index.html to the path mapped to the Swagger UI servlet
     * - Voila!
     *
     * <p>Note: Current configuration will make ALL endpoints (Resource endpoints, admin endpoints, etc.) available
     * in Swagger. Execution of the admin endpoints does not work due to path issues.
     *
     * @param environment - Jersey's environment
     */
    private void registerSwaggerAtAdminPath(final Environment environment) {

        // Add the Open API servlet to the dropwizard admin environment and map it
        // Open API servlet path: http://localhost:8080/admin/openapi
        ServletRegistration.Dynamic openApiAdminServlet = environment.admin().addServlet(OpenApiServlet.class.getSimpleName(), new OpenApiServlet());
        openApiAdminServlet.addMapping("/openapi");

        // Add the Swagger UI servlet to the dropwizard admin environment and map it
        // Swagger UI path: http://localhost:8080/admin/swagger
        ServletRegistration.Dynamic swaggerAdminServlet = environment.admin().addServlet("Swagger UI Admin Servlet",
                new AssetServlet("swagger", "/swagger", "index.html", Charsets.UTF_8));
        swaggerAdminServlet.addMapping("/swagger", "/swagger/*");
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

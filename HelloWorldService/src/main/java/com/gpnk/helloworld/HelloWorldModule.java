package com.gpnk.helloworld;

import com.gpnk.common.GPNKModule;
import com.gpnk.config.HelloWorldProperties;
import com.gpnk.persistence.FakeLocationDAO;
import com.gpnk.persistence.FakeUserDAO;
import com.gpnk.persistence.LocationDAO;
import com.gpnk.persistence.UserDAO;

import com.google.inject.name.Names;

/**
 * Bindings for HelloWorld.
 */
public class HelloWorldModule extends GPNKModule {

    @Override
    protected void config() {
        HelloWorldProperties config = new HelloWorldProperties();

        // this demonstrates @Named injection
        // if it were not for purposes of demonstration, Nadya would have preferred to inject the HelloWorldProperties
        // object into the constructor of the HelloWorldResource class to preserve encapsulation.
        bind(String.class).annotatedWith(Names.named("nameOnlyTemplate")).toInstance(config.getNameOnlyTemplate());
        bind(String.class).annotatedWith(Names.named("weatherTemplate")).toInstance(config.getWeatherTemplate());
        bind(String.class).annotatedWith(Names.named("defaultName")).toInstance(config.getDefaultName());

        bind(UserDAO.class).to(FakeUserDAO.class);
        bind(LocationDAO.class).to(FakeLocationDAO.class);

        bindHealthCheckable(FakeUserDAO.class);

        bindResource(HelloWorldResource.class);
    }
}

package com.gpnk.helloworld;

import com.gpnk.common.GPNKModule;
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
        bind(String.class).annotatedWith(Names.named("nameOnlyTemplate")).toInstance("Howdy, %s!");
        bind(String.class).annotatedWith(Names.named("weatherTemplate")).toInstance("Hello %s, the weather is %s!");
        bind(String.class).annotatedWith(Names.named("defaultName")).toInstance("Friend");

        bind(UserDAO.class).to(FakeUserDAO.class);
        bind(LocationDAO.class).to(FakeLocationDAO.class);

        bindResource(HelloWorldResource.class);
    }
}

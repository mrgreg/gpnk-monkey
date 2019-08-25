package com.gpnk.helloworld;

import com.google.inject.name.Names;
import com.gpnk.common.GPNKModule;

/**
 * Bindings for HelloWorld.
 */
public class HelloWorldModule extends GPNKModule {

    @Override
    protected void config() {
        bind(String.class).annotatedWith(Names.named("template")).toInstance("Howdy, %s!");
        bind(String.class).annotatedWith(Names.named("defaultName")).toInstance("Friend");

        bindResource(HelloWorldResource.class);
    }
}

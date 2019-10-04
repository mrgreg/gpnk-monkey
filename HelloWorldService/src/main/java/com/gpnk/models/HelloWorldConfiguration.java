package com.gpnk.models;

import com.gpnk.config.ConfigUtil;

import com.typesafe.config.Config;
import io.dropwizard.Configuration;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotEmpty;

public class HelloWorldConfiguration extends Configuration {

    @Getter
    @NotEmpty
    private List<String> modules;

    @Getter
    private String name;

    /**
     * Loads TypeSafe config from reference.conf and application.conf files.
     */
    public HelloWorldConfiguration() {
        Config config = ConfigUtil.load();
        Config appConfig = config.getConfig("application");

        this.modules = appConfig.getStringList("modules");
        this.name = appConfig.getString("name");
    }

    @Override
    public String toString() {
        String configStr = "HelloWorldConfiguration{modules = " + Arrays.toString(modules.toArray()) + ", "
                + "name = " + name;
        return configStr + "\n" + super.toString();
    }

}

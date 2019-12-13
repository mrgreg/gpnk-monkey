package com.gpnk.config;

import com.typesafe.config.Config;
import lombok.Getter;

/**
 * Configuration properties for HelloWorld which are loaded from {@code application.conf}.
 */
public class HelloWorldProperties {

    @Getter
    private String defaultName;
    @Getter
    private String nameOnlyTemplate;
    @Getter
    private String weatherTemplate;

    /**
     * Loads properties from {@code application.conf}.
     */
    public HelloWorldProperties() {
        Config config = ConfigUtil.load();
        Config hwConfig = config.getConfig("helloworld");

        this.defaultName = hwConfig.getString("defaultName");
        this.nameOnlyTemplate = hwConfig.getString("nameOnlyTemplate");
        this.weatherTemplate = hwConfig.getString("weatherTemplate");
    }
}

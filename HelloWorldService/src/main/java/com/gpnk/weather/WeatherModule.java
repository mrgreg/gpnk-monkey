package com.gpnk.weather;

import com.gpnk.common.GPNKModule;
import com.gpnk.config.ConfigUtil;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.typesafe.config.Config;

/**
 * Bindings for WeatherServiceClient
 */
public class WeatherModule extends GPNKModule {
    @Override
    protected void config() {
        bind(WeatherServiceClient.class).to(DarkSkyWeatherServiceClient.class);
        bindHealthCheckable(DarkSkyWeatherServiceClient.class);
    }

    /**
     * Produces the dark sky secret key.
     * You can get your own key here: https://darksky.net/dev
     */
    @Singleton
    @Provides
    @Named("darkSkySecretKey")
    public String getDarkSkySecretKey() {

        Config config = ConfigUtil.load();
        Config weatherConfig = config.getConfig("weather");

        String darkSkyApiKey = weatherConfig.getString("darkSkyApiKey");
        return darkSkyApiKey;
    }
}

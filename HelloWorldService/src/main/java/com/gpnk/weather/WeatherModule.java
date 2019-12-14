package com.gpnk.weather;

import com.gpnk.common.GPNKModule;

import com.google.inject.Provides;
import com.google.inject.name.Named;

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
     * Produces the dark sky secret key
     */
    @Provides
    @Named("darkSkySecretKey")
    public String getDarkSkySecretKey() {
        // TODO: get this from config
        // This is an old api key for an account I signed up for.  It is no longer valid.
        // Get your own key here: https://darksky.net/dev
        return "1b06b5c498df5d71efac20db9ea91fd6";
    }
}

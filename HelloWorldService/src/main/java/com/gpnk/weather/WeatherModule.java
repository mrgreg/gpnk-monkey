package com.gpnk.weather;

import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.gpnk.common.GPNKModule;

/**
 * Bindings for WeatherServiceClient
 */
public class WeatherModule extends GPNKModule {
    @Override
    protected void config() {
        bind(WeatherServiceClient.class).to(DarkSkyWeatherServiceClient.class);
    }

    /**
     * Produces the dark sky secret key
     */
    @Provides
    @Named("darkSkySecretKey")
    public String getDarkSkySecretKey() {
        // TODO: get this from config
        // This is the api key for an account I signed up for.  The first 1000 calls per day are free
        return "1b06b5c498df5d71efac20db9ea91fd6";
    }
}

package com.gpnk.weather;

import com.gpnk.models.Location;
import com.gpnk.models.WeatherReport;

import java.util.Optional;

/**
 * An implementation of WeatherServiceClient that returns canned data
 */
public class FakeWeatherServiceClient implements WeatherServiceClient {

    @Override
    public Optional<WeatherReport> getWeatherForLocation(final Location location) {

        if ("90210".equals(location.getZipCode())) {
            return Optional.of(new WeatherReport(77, "sunny"));
        }

        return Optional.empty();
    }

}

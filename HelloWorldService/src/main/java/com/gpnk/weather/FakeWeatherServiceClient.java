package com.gpnk.weather;

import com.gpnk.models.Location;
import com.gpnk.models.WeatherReport;

import com.codahale.metrics.health.HealthCheck;

import java.util.Optional;

/**
 * An implementation of WeatherServiceClient that returns canned data
 */
public class FakeWeatherServiceClient implements WeatherServiceClient {

    /** {@inheritDoc} */
    @Override
    public Optional<WeatherReport> getWeatherForLocation(final Location location) {

        if ("90210".equals(location.getZipCode())) {
            return Optional.of(new WeatherReport(77, "sunny"));
        }

        return Optional.empty();
    }

    /** {@inheritDoc} */
    @Override
    public HealthCheck.Result getHealth() {
        return HealthCheck.Result.healthy();
    }
}

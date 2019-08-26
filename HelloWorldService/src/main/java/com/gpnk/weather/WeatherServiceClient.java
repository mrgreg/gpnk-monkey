package com.gpnk.weather;

import com.gpnk.models.Location;
import com.gpnk.models.WeatherReport;

import java.util.Optional;

/**
 * A client for looking up the Weather
 * // TODO: turn this into a core service and use gRPC to call it
 */
public interface WeatherServiceClient {

    /**
     * Gets the current weather for a given zipcode
     */
    Optional<WeatherReport> getWeatherForLocation(Location location);
}

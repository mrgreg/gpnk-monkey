package com.gpnk.weather;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.gpnk.models.Location;
import com.gpnk.models.WeatherReport;
import org.slf4j.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

/**
 * Implementation of WeatherServiceClient that is backed by Dark Sky API
 */
public class DarkSkyWeatherServiceClient implements WeatherServiceClient {

    // https://api.darksky.net/forecast/1b06b5c498df5d71efac20db9ea91fd6/37.8267,-122.4233

    private static final String API_URL = "https://api.darksky.net/forecast/{key}/{latitude},{longitude}";

    @Inject
    private Logger log;

    @Inject
    @Named("darkSkySecretKey")
    private String secretKey;


    @SuppressWarnings("PMD.CloseResource")
    @Override
    public Optional<WeatherReport> getWeatherForLocation(final Location location) {

        Response response = ClientBuilder.newClient()
                .target(API_URL)
                .resolveTemplate("key", secretKey)
                .resolveTemplate("latitude", location.getLatitude())
                .resolveTemplate("longitude", location.getLongitude())
                .queryParam("exclude", "[minutely,hourly,daily,alerts,flags]]")
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            log.error("Got non 200 ({}) status from Dark Sky API: {}", response.getStatus(), response.getEntity());
            return Optional.empty();
        }

        // Re: PMD.CloseResource - response is closed within the readEntity method.
        WeatherReport weatherReport = response.readEntity(WeatherReport.class);
        return Optional.of(weatherReport);
    }
}

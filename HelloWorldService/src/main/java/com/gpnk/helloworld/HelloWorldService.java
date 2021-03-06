package com.gpnk.helloworld;

import com.gpnk.common.response.examples.CustomResponseCodeException;
import com.gpnk.common.response.examples.FullyCustomizedSampleException;
import com.gpnk.models.Location;
import com.gpnk.models.User;
import com.gpnk.models.WeatherReport;
import com.gpnk.persistence.LocationDAO;
import com.gpnk.persistence.UserDAO;
import com.gpnk.weather.WeatherServiceClient;

import com.google.inject.Inject;

import java.util.Optional;

/**
 * A service for the business logic of HelloWorld
 */
public class HelloWorldService {

    @Inject
    private UserDAO userDAO;

    @Inject
    private LocationDAO locationDAO;

    @Inject
    private WeatherServiceClient weatherServiceClient;

    /**
     * Default Constructor for DI
     */
    public HelloWorldService() {
    }

    /**
     * Returns a WeatherReport for the user's location, if it can be determined.
     */
    public Optional<WeatherReport> getWeatherForUser(final String userName) {

        if ("foo".equals(userName)) {
            throw new CustomResponseCodeException("CustomResponseCodeException message.");
        }

        if ("bar".equals(userName)) {
            throw new FullyCustomizedSampleException("Exception like no other :) Demos a fully customized ExceptionMapper.");
        }

        Optional<User> user = userDAO.getUserByName(userName);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        Optional<Location> location = locationDAO.getLocationByZip(user.get().getZipCode());

        return location.map(weatherServiceClient::getWeatherForLocation).get();
    }

    /**
     * Saves a new user.
     */
    public void createUser(final User user) {
        userDAO.createUser(user);
    }

}

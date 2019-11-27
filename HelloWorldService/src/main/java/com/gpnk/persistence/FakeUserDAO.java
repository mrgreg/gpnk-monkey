package com.gpnk.persistence;

import com.gpnk.common.HealthCheckable;
import com.gpnk.models.User;

import com.codahale.metrics.health.HealthCheck;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An implementation of UserDAO backed by a HashMap.
 */
public class FakeUserDAO implements UserDAO, HealthCheckable {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public Optional<User> getUserByName(final String name) {
        return Optional.ofNullable(users.get(name));
    }

    @Override
    public void createUser(final User user) {
        users.put(user.getName(), user);
    }

    @Override
    public HealthCheck.Result getHealth() {
        return HealthCheck.Result.healthy("I'm sad because I'm not real");
    }
}

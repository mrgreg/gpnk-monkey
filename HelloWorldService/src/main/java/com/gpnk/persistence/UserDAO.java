package com.gpnk.persistence;

import com.gpnk.models.User;

import java.util.Optional;

/**
 * DAO for Users
 */
public interface UserDAO {

    /**
     * Looks up a user by name
     */
    Optional<User> getUserByName(String name);

    /**
     * Creates a new User
     */
    void createUser(User user);
}

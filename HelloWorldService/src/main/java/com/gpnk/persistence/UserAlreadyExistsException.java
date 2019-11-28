package com.gpnk.persistence;

/**
 * Is thrown when creating a new user with a name that already exists in the database.
 */
public class UserAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 20191129143824L;

    /**
     * @param msg Error message with user name
     */
    public UserAlreadyExistsException(final String msg) {
        super(msg);
    }
}

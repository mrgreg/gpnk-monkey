package com.gpnk.persistence;

import com.gpnk.models.Location;

import java.util.Optional;

/**
 * A DAO for location info
 */
public interface LocationDAO {

    /**
     * Looks up a location by zipcode
     * @return
     */
    Optional<Location> getLocationByZip(String zipCode);
}

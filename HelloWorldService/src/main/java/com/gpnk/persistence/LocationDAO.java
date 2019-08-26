package com.gpnk.persistence;

import com.gpnk.models.Location;

/**
 * A DAO for location info
 */
public interface LocationDAO {

    /**
     * Looks up a location by zipcode
     */
    Location getLocationByZip(String zipCode);
}

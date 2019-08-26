package com.gpnk.persistence;

import com.gpnk.models.Location;

/**
 * An implementation of LocationDAO with canned fake info.
 * The real impl should be backed by a database table with info from here:
 * https://public.opendatasoft.com/explore/dataset/us-zip-code-latitude-and-longitude/export/
 */
public class FakeLocationDAO implements LocationDAO {
    @Override
    public Location getLocationByZip(final String zipCode) {
        return new Location(zipCode, 34.089, -118.406);
    }
}

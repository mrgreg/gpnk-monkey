package com.gpnk.persistence;

import com.gpnk.db.BaseDAO;
import com.gpnk.generated.jooq.Tables;
import com.gpnk.models.Location;

import com.google.inject.Inject;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.impl.TableImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LocationDAOImpl extends BaseDAO implements LocationDAO {

    /**
     * Constructor passes the DSLContext to BaseDAO
     */
    @Inject
    public LocationDAOImpl(final DSLContext db) {
        super(db);
    }

    @Override
    protected List<TableImpl> getTables() {
        return Arrays.asList(Tables.LOCATIONS);
    }

    @Override
    public Optional<Location> getLocationByZip(String zipCode) {

        SelectConditionStep results = getLocationByZipQuery(zipCode);
        Record locationRecord = results.fetchOne();
        if (locationRecord != null) {
            return Optional.of(locationRecord.into(Location.class));
        }

        return Optional.empty();
    }

    private SelectConditionStep getLocationByZipQuery(String zipCode) {
        return db.select(
                Tables.LOCATIONS.ZIP_CODE,
                Tables.LOCATIONS.LATITUDE,
                Tables.LOCATIONS.LONGITUDE
        )
                .from(Tables.LOCATIONS)
                .where(Tables.LOCATIONS.ZIP_CODE.equal(zipCode));
    }
}

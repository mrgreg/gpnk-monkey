package com.gpnk.persistence;

import com.gpnk.db.BaseDAO;
import com.gpnk.generated.jooq.Tables;
import com.gpnk.generated.jooq.tables.records.UsersRecord;
import com.gpnk.models.User;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.impl.TableImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl extends BaseDAO implements UserDAO {

    /**
     * Constructor passes the DSLContext to BaseDAO
     */
    @Inject
    public UserDAOImpl(final DSLContext db) {
        super(db);
    }

    @Override
    protected List<TableImpl> getTables() {
        return Arrays.asList(Tables.USERS);
    }

    /** {@inheritDoc} */
    @Override
    @Timed
    public Optional<User> getUserByName(String name) {
        SelectConditionStep results = getUserByNameQuery(name);
        Record userRecord = results.fetchOne();
        if (userRecord != null) {
            return Optional.of(userRecord.into(User.class));
        }

        return Optional.empty();
    }

    /** {@inheritDoc} */
    @Override
    @Timed
    public void createUser(User user) {
        if (userExistsByName(user.getName())) {
            throw new UserAlreadyExistsException("User " + user.getName() + " already exists");
        }

        UsersRecord record = db.newRecord(Tables.USERS);
        record.setName(user.getName());
        record.setZipCode(user.getZipCode());

        record.store();

    }

    private boolean userExistsByName(String name) {
        Optional<User> existingUser = getUserByName(name);
        return existingUser.map(u -> true).orElse(false);
    }

    protected SelectConditionStep getUserByNameQuery(String name) {
        return db.select(
                Tables.USERS.NAME,
                Tables.USERS.ZIP_CODE
        )
                .from(Tables.USERS)
                .where(Tables.USERS.NAME.equalIgnoreCase(name));
    }

}

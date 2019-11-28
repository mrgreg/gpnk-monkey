package com.gpnk.db;

import com.gpnk.common.GPNKModule;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.jooq.DSLContext;

import javax.sql.DataSource;

public class DbModule extends GPNKModule {

    @Override
    protected void config() {
        bindStartupHook(FlywayStartupHook.class);
    }

    /**
     * @return Provides the Database Config.
     */
    @Singleton
    @Provides
    public DbConfig provideDbConfig() {
        return new DbConfig();
    }

    /**
     * @return Provides the DataSource object based on the given {@code config}.
     */
    @Provides
    public DataSource provideDataSource(DbConfig config) {
        return config.getDataSource();
    }

    /**
     * @return Provides the DSLContext object based on the given {@code config}.
     */
    @Provides
    public DSLContext provideDSLContext(DbConfig config) {
        return config.getDslContext();
    }

}

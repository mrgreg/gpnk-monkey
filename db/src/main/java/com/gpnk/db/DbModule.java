package com.gpnk.db;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import javax.sql.DataSource;

public class DbModule extends AbstractModule {

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

}

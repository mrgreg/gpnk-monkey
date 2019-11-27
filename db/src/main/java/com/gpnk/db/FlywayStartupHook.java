package com.gpnk.db;

import com.gpnk.common.ApplicationStartupHook;

import com.google.inject.Inject;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;

import javax.sql.DataSource;

public class FlywayStartupHook implements ApplicationStartupHook {

    @Inject
    private Logger log;

    @Inject
    private DataSource dataSource;

    @Override
    public void onStartup() {

        log.info("Calling flyway ...");

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();

    }
}

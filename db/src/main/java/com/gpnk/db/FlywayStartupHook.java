package com.gpnk.db;

import com.gpnk.common.ApplicationStartupHook;

import com.google.inject.Inject;
import org.slf4j.Logger;

public class FlywayStartupHook implements ApplicationStartupHook {

    @Inject
    private Logger log;

//    @Inject
//    DataSource dataSource;

    @Override
    public void onStartup() {

        // TODO: add flyway call here
        log.info("Calling flyway ...");

    }
}

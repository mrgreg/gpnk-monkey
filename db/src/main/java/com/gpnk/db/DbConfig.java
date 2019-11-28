package com.gpnk.db;

import com.gpnk.config.ConfigUtil;

import com.typesafe.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class DbConfig {

    @Getter
    private HikariDataSource dataSource;
    @Getter
    private DSLContext dslContext;

    protected DbConfig() {
        Config config = ConfigUtil.load();
        Config dbConfig = config.getConfig("db");
        setupDataSource(dbConfig);
    }

    private void setupDataSource(Config dbConfig) {

        String subprotocol = "postgresql";
        String dbHost = dbConfig.getString("host");
        int dbPort = dbConfig.getInt("port");
        String schema = dbConfig.getString("schema");
        String driverClassName = "org.postgresql.Driver";
        String dbUsername = dbConfig.getString("username");
        String dbPassword = dbConfig.getString("password");
        boolean autoCommit = dbConfig.getBoolean("autoCommit");

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:" + subprotocol + "://" +  dbHost + ":" + dbPort + "/" + schema);
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setUsername(dbUsername);
        hikariConfig.setPassword(dbPassword);
        hikariConfig.setAutoCommit(autoCommit);
        //TODO: possibly add codahale healthCheck and metrics to the datasource

        // TODO: we may want to tune up the Connection pool in the future
//        hikariConfig.addDataSourceProperty( "cachePrepStmts" , "true" );
//        hikariConfig.addDataSourceProperty( "prepStmtCacheSize" , "250" );
//        hikariConfig.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );

        this.dataSource = new HikariDataSource(hikariConfig);

        SQLDialect dialect = SQLDialect.POSTGRES;
        this.dslContext = DSL.using(dataSource, dialect);
    }

}

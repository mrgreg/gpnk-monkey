package com.gpnk.db;

import com.gpnk.common.HealthCheckable;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;
import org.jooq.DSLContext;
import org.jooq.SelectLimitPercentStep;
import org.jooq.impl.TableImpl;
import org.slf4j.Logger;

import java.util.List;

/**
 * The base class for DAOs that implements the health check.
 */
public abstract class BaseDAO implements HealthCheckable {

    @Inject
    private Logger log;

    protected DSLContext db;

    protected BaseDAO(final DSLContext db) {
        this.db = db;
    }

    /**
     * The implementing DAOs will return the list of tables they use in the database.
     * @return list of tables used by the DAO
     */
    protected abstract List<TableImpl> getTables();

    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis"})
    @Override
    public HealthCheck.Result getHealth() {

        List<TableImpl> tables = getTables();
        HealthCheck.ResultBuilder unhealthyResult = HealthCheck.Result.builder().unhealthy()
                .withMessage("Can not access database tables. Check details below.");
        boolean isHealthy = true;

        for (TableImpl table : tables) {
            try (SelectLimitPercentStep select = db.selectOne().from(table).limit(1)) {
                // need to make the following fetch otherwise the above SQL statement will not be executed against
                // database due to lazy evaluation
                select.fetchAny();
            } catch (Exception e) {
                log.error("Error accessing database table \"" + table.getName() + "\":", e);
                unhealthyResult.withDetail("table \"" + table.getName() + "\"", e.getMessage());
                isHealthy = false;
            }
        }

        if (isHealthy) {
            return HealthCheck.Result.builder()
                    .healthy()
                    .build();
        } else {
            return unhealthyResult.build();
        }
    }

}

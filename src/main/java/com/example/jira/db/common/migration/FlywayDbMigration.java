package com.example.jira.db.common.migration;

import jakarta.annotation.*;
import org.flywaydb.core.*;
import org.flywaydb.core.api.configuration.*;

import javax.sql.*;

public class FlywayDbMigration implements DbMigration {
    private final DataSource ds;

    public FlywayDbMigration(DataSource ds) {
        this.ds = ds;
    }

    @Override
    @PostConstruct
    public void migrate() {
        ClassicConfiguration config = new ClassicConfiguration();
        config.setBaselineOnMigrate(true);
        config.setTable("schema_version");
        config.setDataSource(ds);

        new Flyway(config).migrate();
    }
}

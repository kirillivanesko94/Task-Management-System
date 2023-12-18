package com.example.jira.db.common.migration;

@FunctionalInterface
public interface DbMigration {
    void migrate();
}

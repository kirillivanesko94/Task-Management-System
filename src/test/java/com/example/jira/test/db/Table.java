package com.example.jira.test.db;

public enum Table {
    COMMENT("task_comment"),
    PLATFORM_USER("platform_user"),
    TASK("task");

    public final String tableName;

    Table(String tableName) {
        this.tableName = tableName;
    }
}

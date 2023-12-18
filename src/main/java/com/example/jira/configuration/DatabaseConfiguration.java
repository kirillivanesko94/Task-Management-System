package com.example.jira.configuration;

import com.example.jira.db.common.migration.*;
import com.zaxxer.hikari.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;

import javax.sql.*;

@Configuration
public class DatabaseConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "db")
    public DbConfig dbConfig() {
        return new DbConfig();
    }

    @Bean
    public DataSource dataSource(DbConfig cfg) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(cfg.getJdbcUrl());
        hikariConfig.setUsername(cfg.getUsername());
        hikariConfig.setPassword(cfg.getPassword());
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.setPoolName("Fake Jira Pool");
        hikariConfig.addDataSourceProperty("ApplicationName", "Fake Jira");
        // Конфигурируем connection pool
        hikariConfig.setMaximumPoolSize(cfg.getMaximumPoolSize());
        hikariConfig.setMinimumIdle(cfg.getMinimumIdle());
        hikariConfig.setIdleTimeout(cfg.getIdleTimeout());
        hikariConfig.setConnectionTimeout(cfg.getConnectionTimeout());

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public DbMigration dbMigration(DataSource ds) {
        return new FlywayDbMigration(ds);
    }


}

package com.example.jira.configuration;

/**
 * Конфигурация для настройки DataSource (Connection Pool)
 */
public class DbConfig {
     // строка подключения к БД
     private String jdbcUrl;
     // пользователь
     private String username;
     // пароль
     private String password;
     // максимальное кол-во подключений
     private Integer maximumPoolSize;
     // минимальное кол-во подключений
     private Integer minimumIdle;
     // тайм-аут в ms простоя подключения до того как оно будет закрыто
     private Integer idleTimeout;
     // тайм-аут в ms на получение подключения
     private Integer connectionTimeout;

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public Integer getMinimumIdle() {
        return minimumIdle;
    }

    public void setMinimumIdle(Integer minimumIdle) {
        this.minimumIdle = minimumIdle;
    }

    public Integer getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(Integer idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}

package com.example.jira.test;


import com.example.jira.*;
import com.example.jira.configuration.*;
import com.example.jira.db.common.migration.*;
import com.example.jira.db.user.*;
import com.example.jira.test.db.*;
import com.example.jira.test.rest.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.util.*;
import org.springframework.context.*;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.test.context.*;
import org.testcontainers.containers.*;

import javax.sql.*;

import static com.example.jira.test.BaseFuncTest.Initializer;

@ContextConfiguration(classes = {
    App.class,
    DatabaseConfiguration.class,
    SecurityConfiguration.class,
    BaseFuncTest.TestConfiguration.class
}, initializers = {Initializer.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("FunctionalTest")
public abstract class BaseFuncTest {

    protected PlatformUser USR_TEST = PlatformUser.createNew(
        "test",
        "test@example.com",
        "test"
    );

    public static PostgreSQLContainer<?> dbContainer = createContainer();

    private static PostgreSQLContainer createContainer() {
        PostgreSQLContainer container = new PostgreSQLContainer("postgres:14.5");
        container.addEnv("POSTGRES_DB", "test");
        container.addEnv("POSTGRES_USER", "test");
        container.addEnv("POSTGRES_PASSWORD", "test");

        return container;
    }

    @Autowired
    DbMigration migration;

    @Autowired
    protected TestApi api;

    @BeforeAll
    void setup() {
        migration.migrate();
    }

    @BeforeEach
    void beforeEachSetup() {
        api.clearDb();
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            dbContainer.start();

            String jdbcUrl = dbContainer.getJdbcUrl();

            TestPropertyValues.of(
                "db.jdbcUrl=" + jdbcUrl,
                "db.username=" + dbContainer.getUsername(),
                "db.password=" + dbContainer.getPassword()
            ).applyTo(applicationContext.getEnvironment());
        }
    }

    @Configuration
    static class TestConfiguration {

        @Autowired
        PasswordEncoder passwordEncoder;

        @Bean
        DbHandler dbHandler(DataSource dataSource) {
            return new DbHandler(dataSource);
        }

        @Bean
        TestApi testApi(DbHandler dbHandler) {
            return new TestApi("http://localhost:8080/", dbHandler, passwordEncoder);
        }
    }
}

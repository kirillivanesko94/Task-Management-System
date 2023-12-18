package com.example.jira.test.db;

import com.example.jira.db.task.*;
import com.example.jira.db.user.*;
import org.jdbi.v3.core.*;

import javax.sql.*;
import java.util.*;

public class DbHandler {
    private final Jdbi db;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    private static final List<Table> tablesForClean = new ArrayList<>();
    static {
        tablesForClean.add(Table.COMMENT);
        tablesForClean.add(Table.TASK);
        tablesForClean.add(Table.PLATFORM_USER);
    }

    public DbHandler(DataSource dataSource) {
        this.db = Jdbi.create(dataSource);
        this.userRepository = new JdbiUserRepository(dataSource);
        this.taskRepository = new JdbiTaskRepository(dataSource);
    }

    public void usersExists(Collection<PlatformUser> users) {
        users.forEach(userRepository::insert);
    }

    public void tasksExists(Collection<Task> tasks) {
        tasks.forEach(taskRepository::insert);
    }

    public void clearDb() {
        try (Handle conn = db.open()) {
            tablesForClean.forEach(table -> conn.execute(String.format("delete from %s", table.tableName)));

        }
    }
}

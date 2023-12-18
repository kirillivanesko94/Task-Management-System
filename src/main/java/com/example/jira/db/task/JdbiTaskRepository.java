package com.example.jira.db.task;

import com.example.jira.common.*;
import com.example.jira.db.common.search.*;
import com.example.jira.db.utils.*;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.*;
import org.jdbi.v3.postgres.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class JdbiTaskRepository implements TaskRepository {

    private static final String SQL_CREATE = """
        insert into task(id, title, description, status, priority, author_id, assign_id) 
        values (:id::uuid, :title, :description, :status::task_status, :priority::task_priority, :authorId::uuid, :assignId::uuid)
        """;

    private static final String SQL_GET = """
        select id, title, description, status, priority, author_id, assign_id from task 
        """;

    private static final Map<SearchFilter, JdbiSearchCriteriaDescriptor> FILTER_MAP = new HashMap<>();

    static {
        FILTER_MAP.put(
            SearchFilter.TASK_AUTHOR_ID,
            new JdbiSearchCriteriaDescriptor(
                "authorId",
                "author_id = :authorId::uuid"
            )
        );

        FILTER_MAP.put(
            SearchFilter.TASK_ASSIGN_ID,
            new JdbiSearchCriteriaDescriptor(
                "assignId",
                "assign_id = :assignId::uuid"
            )
        );
    }

    private final Jdbi db;
    private final RowMapper<Task> taskMapper = (rs, ctx) -> {
        Task task = new Task();
        task.setId(DbUtils.stringToUUID(rs.getString("id")));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setStatus(TaskStatus.valueOf(rs.getString("status")));
        task.setPriority(TaskPriority.valueOf(rs.getString("priority")));
        task.setAuthorId(DbUtils.stringToUUID(rs.getString("author_id")));
        task.setAssignId(DbUtils.stringToUUID(rs.getString("assign_id")));

        return task;
    };


    public JdbiTaskRepository(DataSource ds) {
        this.db = Jdbi.create(ds)
            .installPlugin(new PostgresPlugin());
    }

    @Override
    public void insert(Task task) {
        try (Handle conn = db.open()) {
            conn.createUpdate(SQL_CREATE)
                    .bind("id", task.getId())
                    .bind("title", task.getTitle())
                    .bind("description", task.getDescription())
                    .bind("status", task.getStatus().name())
                    .bind("priority", task.getPriority().name())
                    .bind("authorId", task.getAuthorId())
                    .bind("assignId", task.getAssignId())
                    .execute();
        }
    }


    @Override
    public Optional<Task> findById(UUID id) {
        try (Handle conn = db.open()) {
            return conn
                    .createQuery(SQL_GET + " where id = :id::uuid")
                    .bind("id", id)
                    .map(taskMapper)
                    .findFirst();
        }
    }

    @Override
    public PagedData<Task> findByCriteria(SearchCriteria criteria) {
        try (Handle conn = db.open()) {
            List<Task> tasks = JdbiSearchCriteriaQueryBuilder
                .buildQuery(conn, SQL_GET, "title", criteria, FILTER_MAP)
                .map(taskMapper)
                .stream()
                .toList();

            return new PagedData<>(
                tasks,
                criteria.getLimit(),
                criteria.getOffset()
            );
        }
    }

    @Override
    public void deleteById(UUID id) {
        try (Handle conn = db.open()) {
            conn
                    .createUpdate("delete from task where id = :id::uuid")
                    .bind("id", id)
                    .execute();
        }
    }

    @Override
    public void update(Task task) {
        try (Handle conn = db.open()) {
            conn.createUpdate("update task set title = :title, description = :description, status = :status, " +
                            "priority = :priority, author_id = :author_id, assign_id = :assign_id where id = :id::uuid")
                    .bind("id", task.getId())
                    .bind("title", task.getTitle())
                    .bind("description", task.getDescription())
                    .bind("status", task.getStatus())
                    .bind("priority", task.getPriority())
                    .bind("author_id", task.getAuthorId())
                    .bind("assign_id", task.getAssignId())
                    .execute();
        }
    }
}

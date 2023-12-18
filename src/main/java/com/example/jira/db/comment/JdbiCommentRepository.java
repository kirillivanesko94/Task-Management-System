package com.example.jira.db.comment;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.UUID;
@Repository
public class JdbiCommentRepository implements CommentRepository{
    private final Jdbi db;

    public JdbiCommentRepository(DataSource ds) {
        this.db = Jdbi.create(ds);
    }

    @Override
    public void insert(Comment comment) {
        try (Handle conn = db.open()) {
            conn.createUpdate("insert into task_comment(id, body, author_id, task_id) values (:id::uuid, :body, :author_id, :task_id)")
                    .bind("id", comment.getId())
                    .bind("body", comment.getBody())
                    .bind("author_id", comment.getAuthorId())
                    .bind("task_id", comment.getTaskId())
                    .execute();
        }
    }

    @Override
    public Comment getById(UUID id) {
        try (Handle conn = db.open()) {
            return conn
                    .createQuery("select id, body, author_id, task_id from task_comment where id = :id::uuid")
                    .bind("id", id)
                    .map((rs, ctx) -> {
                        Comment comment = new Comment();
                        comment.setId(UUID.fromString(rs.getString("id")));
                        comment.setBody(rs.getString("body"));
                        comment.setAuthorId(UUID.fromString(rs.getString("author_id")));
                        comment.setTaskId(UUID.fromString(rs.getString("task_id")));

                        return comment;
                    })
                    .findFirst()
                    .orElse(null);
        }
    }

    @Override
    public void deleteById(UUID id) {
        try (Handle conn = db.open()) {
            conn
                    .createUpdate("delete from task_comment where id = :id::uuid")
                    .bind("id", id)
                    .execute();
        }

    }

    @Override
    public void update(Comment comment) {
        try (Handle conn = db.open()) {
            conn.createUpdate("update task_comment set body = :body, author_id = :author_id, task_id = :task_id where id = :id::uuid")
                    .bind("id", comment.getId())
                    .bind("body", comment.getBody())
                    .bind("author_id", comment.getAuthorId())
                    .bind("task_id", comment.getTaskId())
                    .execute();
        }

    }
}

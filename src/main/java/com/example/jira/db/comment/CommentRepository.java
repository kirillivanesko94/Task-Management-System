package com.example.jira.db.comment;

import java.util.UUID;

public interface CommentRepository {
    void insert(Comment comment);

    Comment getById(UUID id);

    void deleteById(UUID id);

    void update(Comment comment);
}

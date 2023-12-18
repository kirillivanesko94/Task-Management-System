package com.example.jira.common;

import java.util.UUID;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(UUID commentId) {
        super(String.format("Comment with id %s not found", commentId));
    }
}

package com.example.jira.db.comment;

import java.util.UUID;

public class Comment {
    private UUID id;
    private String body;
    private UUID authorId;
    private UUID taskId;

    public static Comment createNewComment(String body, UUID authorId, UUID taskId){
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID());
        comment.setBody(body);
        comment.setAuthorId(authorId);
        comment.setTaskId(taskId);
        return comment;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }
}

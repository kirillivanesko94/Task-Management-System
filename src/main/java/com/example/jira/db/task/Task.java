package com.example.jira.db.task;

import java.util.UUID;

public class Task {
    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private UUID authorId;
    private UUID assignId;

    public static Task createNew(
        String title,
        String description,
        TaskPriority priority,
        UUID authorId
    ) {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setStatus(TaskStatus.NEW);
        task.setAuthorId(authorId);

        return task;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public UUID getAssignId() {
        return assignId;
    }

    public void setAssignId(UUID assignId) {
        this.assignId = assignId;
    }
}

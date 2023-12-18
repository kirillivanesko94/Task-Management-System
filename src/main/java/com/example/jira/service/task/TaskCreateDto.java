package com.example.jira.service.task;

import com.example.jira.db.task.*;

public class TaskCreateDto {
    private String title;
    private String description;
    private TaskPriority priority;

    public TaskCreateDto() {
    }

    public TaskCreateDto(String title, String description, TaskPriority priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskPriority getPriority() {
        return priority;
    }
}

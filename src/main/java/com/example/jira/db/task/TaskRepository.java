package com.example.jira.db.task;

import com.example.jira.common.*;

import java.util.*;

public interface TaskRepository {
    void insert(Task task);

    Optional<Task> findById(UUID id);
    PagedData<Task> findByCriteria(SearchCriteria criteria);

    void deleteById(UUID id);

    void update(Task task);
}

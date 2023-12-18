package com.example.jira.service.task;

import com.example.jira.common.*;
import com.example.jira.db.task.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(TaskCreateDto dto, UUID authorId) {
        Task task = Task.createNew(dto.getTitle(), dto.getDescription(), dto.getPriority(), authorId);

        taskRepository.insert(task);

        return task;
    }

    public Task getTaskById(UUID taskId) {
        return taskRepository.findById(taskId)
            .orElseThrow(() -> ObjectNotFoundException.user(taskId));
    }

    public PagedData<Task> findByCriteria(SearchCriteria criteria) {
        return taskRepository.findByCriteria(criteria);
    }
    public void deleteTaskById(UUID id) {
        taskRepository.deleteById(id);
    }
    public void updateTask(TaskCreateDto taskDto, UUID taskId) {
        Task findedTask = getTaskById(taskId);
        findedTask.setDescription(taskDto.getDescription());
        findedTask.setTitle(taskDto.getTitle());
        findedTask.setPriority(taskDto.getPriority());
        taskRepository.update(findedTask);
    }
}

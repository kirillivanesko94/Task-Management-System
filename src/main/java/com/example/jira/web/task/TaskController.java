package com.example.jira.web.task;

import com.example.jira.common.*;
import com.example.jira.db.task.*;
import com.example.jira.scurity.*;
import com.example.jira.service.task.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.jira.common.SearchFilter.*;

@RestController
@RequestMapping("tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    Task create(@RequestBody TaskCreateDto dto) {
        return taskService.createTask(dto, SecurityUtils.getCurrentUserId());
    }

    @GetMapping("{id}")
    Task getById(@PathVariable UUID id) {
        return taskService.getTaskById(id);
    }

    @GetMapping
    PagedData<Task> search(
        @RequestParam(name = "authorId", required = false) UUID authorId,
        @RequestParam(name = "assignId", required = false) UUID assignId,
        @RequestParam(name = "limit", required = false) Integer limit,
        @RequestParam(name = "offset", required = false) Integer offset
    ) {
        SearchCriteria criteria = new SearchCriteria(limit, offset)
            .addFilter(TASK_AUTHOR_ID, authorId == null ? null : authorId.toString())
            .addFilter(TASK_ASSIGN_ID, assignId == null ? null : assignId.toString());

        return taskService.findByCriteria(criteria);

    }
    @DeleteMapping("{id}")
    void deleteById(@PathVariable UUID id) {
        taskService.deleteTaskById(id);
    }
    @PutMapping("{id}")
    void update(@RequestBody TaskCreateDto taskDto, @PathVariable UUID id) {
        taskService.updateTask(taskDto,id);
    }

}

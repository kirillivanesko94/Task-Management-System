package com.example.jira.web.task;

import com.example.jira.common.*;
import com.example.jira.db.task.*;
import com.example.jira.db.user.*;
import com.example.jira.service.task.*;
import com.example.jira.test.*;
import com.example.jira.test.rest.*;
import com.fasterxml.jackson.core.type.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;
import java.util.stream.*;

import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest extends BaseFuncTest {

    private static final String URL_TASKS = "tasks";

    static Stream<Arguments> createArgs() {
        return Stream.of(
            Arguments.of(
                "Major задача",
                new TaskCreateDto(
                    "Это пример заголовка задачи",
                    "Это пример описания задачи",
                    TaskPriority.MAJOR
                )
            )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("createArgs")
    // Тестируем создание задачи с различным набором параметров
    void testCreateTask(
        String title,
        TaskCreateDto dto
    ) {
        // Заводим автора и логинимся под ним
        PlatformUser testUser = USR_TEST;
        String authToken = api.createUserAndReturnAuthToken(USR_TEST);

        // Выполняем запрос на создание
        RestResponse result = api.post(URL_TASKS, dto, authToken);

        // Проверяем что не было ошибок
        assertEquals(200 , result.getCode());
        // Проверяем что ответ API содержит корректную информацию
        Task task = api.convertFromJson(result.getBody(), Task.class);
        assertEquals(dto.getTitle(), task.getTitle());
        assertEquals(dto.getDescription(), task.getDescription());
        assertEquals(dto.getPriority(), task.getPriority());
        // Статус созданной задачи по-умолчанию NEW
        assertEquals(TaskStatus.NEW, task.getStatus());
        // Новая задача ни на кого не назначена
        assertNull(task.getAssignId());
        // Автор задачи -- пользователь из-под которого вызывали API
        assertEquals(testUser.getId(), task.getAuthorId());
    }

    @Test
    void testGetById() {
        // Заводим автора и логинимся под ним
        String authToken = api.createUserAndReturnAuthToken(USR_TEST);

        // Создаем задачу в БД
        Task existsTask = Task.createNew(
            "Заголовок",
            "Описание",
            TaskPriority.MAJOR,
            USR_TEST.getId()
        );
        existsTask.setAssignId(USR_TEST.getId());

        api.taskExists(existsTask);

        // Выполняем запрос на поиск по ID
        RestResponse result = api.get(String.format("%s/%s", URL_TASKS, existsTask.getId()), authToken);

        // Проверяем что не было ошибок
        assertEquals(200 , result.getCode());

        // Проверяем что ответ API содержит корректную информацию
        Task returnedTask = api.convertFromJson(result.getBody(), Task.class);
        assertEquals(existsTask.getId(), returnedTask.getId());
        assertEquals(existsTask.getTitle(), returnedTask.getTitle());
        assertEquals(existsTask.getDescription(), returnedTask.getDescription());
        assertEquals(existsTask.getStatus(), returnedTask.getStatus());
        assertEquals(existsTask.getPriority(), returnedTask.getPriority());
        assertEquals(existsTask.getAuthorId(), returnedTask.getAuthorId());
        assertEquals(existsTask.getAssignId(), returnedTask.getAssignId());

    }


    private static final PlatformUser USR1 = new PlatformUser(
        UUID.fromString("00000000-0000-0000-0000-000000000001"),
        "usr1",
        "user1@example.com",
        "hurr-durr"
    );
    private static final PlatformUser USR2 = new PlatformUser(
        UUID.fromString("00000000-0000-0000-0000-000000000002"),
        "usr2",
        "user2@example.com",
        "hurr-durr"
    );
    private static final List<PlatformUser> allUsers = List.of(USR1, USR2);


    private static Task createTask(String taskNo, PlatformUser author, PlatformUser assign) {
        Task task = Task.createNew(
            String.format("%s. Автор %s", taskNo, author.getId()),
            String.format("Ответственный %s", assign == null ? null : assign.getId() ),
            TaskPriority.BLOCKER,
            author.getId()
        );

        if (assign != null) {
            task.setAssignId(assign.getId());
        }

        return task;
    }


    private static final Task TSK_USR1_USR1 = createTask("TSK-1", USR1, USR1);
    private static final Task TSK_USR1_USR2 = createTask("TSK-2", USR1, USR2);
    private static final Task TSK_USR1_NULL = createTask("TSK-3", USR1, null);
    private static final Task TSK_USR2_USR1 = createTask("TSK-4", USR2, USR1);
    private static final Task TSK_USR2_USR2 = createTask("TSK-5", USR2, USR2);
    private static final Task TSK_USR2_NULL = createTask("TSK-6", USR2, null);

    private static final List<Task> allTasks = List.of(TSK_USR1_USR1, TSK_USR1_USR2, TSK_USR1_NULL,
        TSK_USR2_USR1, TSK_USR2_USR2, TSK_USR2_NULL);

    static Stream<Arguments> searchByCriteriaArgs() {
        return Stream.of(
            Arguments.of(
                "Все задачи без фильтров",
                URL_TASKS,
                allTasks,
                100,
                0
            ),
            Arguments.of(
                "Задачи в которых ответственный USR2",
                String.format("%s?assignId=%s", URL_TASKS, USR1.getId()),
                List.of(TSK_USR1_USR2, TSK_USR2_USR2),
                100,
                0
            ),
            Arguments.of(
                "Только вторая задач где автор USR1",
                String.format("%s?authorId=%s&limit=1&offset=1", URL_TASKS, USR1.getId()),
                List.of(TSK_USR1_USR2),
                1,
                1
            )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("searchByCriteriaArgs")
    void testSearchByCriteria(
        String title,
        String requestUrl,
        List<Task> expectTasks,
        Integer expectLimit,
        Integer expectOffset
    ) {
        // Заводим тестового пользователя и логинимся под ним
        String authToken = api.createUserAndReturnAuthToken(USR_TEST);

        // Создаем всех пользователей
        api.usersExists(allUsers);

        // Создаем все задачи в БД
        api.tasksExists(allTasks);

        // Выполняем запрос
        RestResponse response = api.get(requestUrl, authToken);
        // Проверяем что не было ошибки
        assertEquals(200, response.getCode());

        // Разбираем JSON
        PagedData<Task> pagedData = api.convertFromJson(response.getBody(), new TypeReference<>() {});
        assertEquals(expectLimit, pagedData.getLimit());
        assertEquals(expectOffset, pagedData.getOffset());

        List<UUID> expectTaskIds = expectTasks.stream().map(Task::getId).toList();
        List<UUID> resultTaskIds = expectTasks.stream().map(Task::getId).toList();

        assertEquals(expectTaskIds, resultTaskIds);
    }
    @Test
    void testDeleteById() {
        String authToken = api.createUserAndReturnAuthToken(USR_TEST);
        TaskCreateDto dto = new TaskCreateDto(
                "Это пример заголовка задачи",
                "Это пример описания задачи",
                TaskPriority.MAJOR
        );

        RestResponse createTaskResp = api.post(URL_TASKS, dto, authToken);
        Task currentTask = api.convertFromJson(createTaskResp.getBody(), Task.class);
        RestResponse result = api.delete(String.format("%s/%s", URL_TASKS, currentTask.getId()), authToken);

        assertEquals(200, result.getCode());

    }

    @Test
    void testUpdateTask() {
        String authToken = api.createUserAndReturnAuthToken(USR_TEST);

        TaskCreateDto dto = new TaskCreateDto(
                "Это пример заголовка задачи",
                "Это пример описания задачи",
                TaskPriority.MAJOR
        );

        TaskCreateDto newDto = new TaskCreateDto("Новый заголовок",
                "Новое описание",
                TaskPriority.TRIVIAL);

        RestResponse createTaskResp = api.post(URL_TASKS, dto, authToken);
        Task currentTask = api.convertFromJson(createTaskResp.getBody(), Task.class);

        RestResponse result = api.put(String.format("%s/%s", URL_TASKS, currentTask.getId()), newDto, authToken);

        RestResponse requestForFModifiedTask = api.get(String.format("%s/%s", URL_TASKS, currentTask.getId()), authToken);
        Task resultTask = api.convertFromJson(requestForFModifiedTask.getBody(), Task.class);
        assertEquals(200, result.getCode());

        assertEquals(newDto.getDescription(), resultTask.getDescription());
        assertEquals(newDto.getTitle(), resultTask.getTitle());
        assertEquals(newDto.getPriority(), resultTask.getPriority());
    }
}

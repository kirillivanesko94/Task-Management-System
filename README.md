# Task management system


## Сборка и запуск приложения:

Собираем и запускаем приложение
```
docker compose up -d
```

Создаем пользователя:
```bash
curl -v -H 'Content-Type: application/json' \
    -X POST http://localhost:8080/auth/register \
    -d '{"name":"valera", "login":"valera@example.com", "password":"valera"}'
```

Логинимся под пользователем:
```bash
curl -v -H 'Content-Type: application/json' \
    -X POST http://localhost:8080/auth/login \
    -d '{"login":"valera@example.com", "password":"valera"}' 
```

В ответе будет возвращен JWT-токен:
```bash
{"authToken":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2YWxlcmFAZXhhbXBsZS5jb20iLCJyb2xlIjpbXSwiaWF0IjoxNzAyOTE1NDg4LCJleHAiOjE3MDI5NTE0ODh9.vyhAUMzSbwKpOO0eU4gv1scQaf5611J2xIqDyBENOiw"}
```

Для удобства лучше сохранить его в env-переменную:
```bash
export FJIRA_TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2YWxlcmFAZXhhbXBsZS5jb20iLCJyb2xlIjpbXSwiaWF0IjoxNzAyOTE1NDg4LCJleHAiOjE3MDI5NTE0ODh9.vyhAUMzSbwKpOO0eU4gv1scQaf5611J2xIqDyBENOiw"
```

Получение списка задач:
```bash
curl -v -H 'Content-Type: application/json' \
    -H "Authorization: Bearer $FJIRA_TOKEN" \
    http://localhost:8080/tasks 
```

Создание новой задачи:
```bash
curl -v -H 'Content-Type: application/json' \
    -H "Authorization: Bearer $FJIRA_TOKEN" \
    -X POST http://localhost:8080/tasks \
    -d '{"title": "Пример задачи", "description": "Описание", "priority": "MINOR"}' 
```
## Technical stack:
* java 17
* docker
* flyWay
* Spring (Boot, Security)
* JWT token


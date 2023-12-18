package com.example.jira.common;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String object, Object findByClause) {
        super(String.format("Object %s not found by %s", object, findByClause));
    }

    public static ObjectNotFoundException user(Object findClause) {
        return new ObjectNotFoundException("User", findClause);
    }
}

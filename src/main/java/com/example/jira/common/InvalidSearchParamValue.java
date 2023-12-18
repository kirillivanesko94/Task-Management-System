package com.example.jira.common;

public class InvalidSearchParamValue extends RuntimeException {

    public InvalidSearchParamValue(String field, Integer value) {
        super(String.format("Invalid %s param value %d.", field, value));
    }
}

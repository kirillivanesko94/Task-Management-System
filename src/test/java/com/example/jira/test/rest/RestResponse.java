package com.example.jira.test.rest;

public class RestResponse {
    private final Integer code;
    private final String body;

    public RestResponse(Integer code, String body) {
        this.code = code;
        this.body = body;
    }

    public Integer getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }
}

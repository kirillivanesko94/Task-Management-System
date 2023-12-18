package com.example.jira.service.comment;

public class CommentDto {
    private String body;
    public CommentDto() {

    }
    public CommentDto(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }
}

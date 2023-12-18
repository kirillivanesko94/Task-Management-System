package com.example.jira.web.auth;

public class TokenResponse {
    private String authToken;

    public TokenResponse() {}

    public TokenResponse(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}

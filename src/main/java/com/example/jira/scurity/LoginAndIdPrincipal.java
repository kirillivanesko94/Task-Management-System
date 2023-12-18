package com.example.jira.scurity;

import java.util.*;

public class LoginAndIdPrincipal {
    private final UUID id;
    private final String login;

    public LoginAndIdPrincipal(UUID id, String login) {
        this.id = id;
        this.login = login;
    }

    public UUID getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }
}

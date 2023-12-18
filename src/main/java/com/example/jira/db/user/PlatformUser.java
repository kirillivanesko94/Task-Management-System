package com.example.jira.db.user;

import java.util.*;

public class PlatformUser {
    private UUID id;
    private String name;
    private String login;
    private String password;

    public PlatformUser() {
    }

    public PlatformUser(UUID id, String name, String login, String password) {
        this();
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public static PlatformUser createNew(
        String name,
        String login,
        String pwd
    ) {
        PlatformUser user = new PlatformUser();
        user.setId(UUID.randomUUID());
        user.setName(name);
        user.setLogin(login);
        user.setPassword(pwd);

        return user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

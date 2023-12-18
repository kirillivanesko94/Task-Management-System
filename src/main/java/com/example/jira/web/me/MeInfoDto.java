package com.example.jira.web.me;

import com.example.jira.db.user.*;

import java.util.*;

public class MeInfoDto {
    private final UUID id;
    private final String name;

    public MeInfoDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public MeInfoDto(PlatformUser user) {
        this(user.getId(), user.getName());
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

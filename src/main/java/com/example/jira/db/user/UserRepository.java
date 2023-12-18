package com.example.jira.db.user;

import java.util.*;

public interface UserRepository {
    void insert(PlatformUser platformUser);

    Optional<PlatformUser> findById(UUID id);
    Optional<PlatformUser> findByLogin(String login);

    void update(PlatformUser platformUser);

    void deleteById(UUID id);
}

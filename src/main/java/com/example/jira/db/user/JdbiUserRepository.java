package com.example.jira.db.user;

import org.jdbi.v3.core.*;
import org.jdbi.v3.core.mapper.*;
import org.springframework.stereotype.*;

import javax.sql.*;
import java.util.*;

@Repository
@SuppressWarnings("java:S2095")
public class JdbiUserRepository implements UserRepository {

    private static final String SQL_GET = """
        select id, name, login, pwd 
        from platform_user
        """;

    private static final String SQL_GET_BY_ID = SQL_GET + " where id = :id::uuid ";
    private static final String SQL_GET_BY_LOGIN = SQL_GET + " where login = :login";

    private final RowMapper<PlatformUser> userMapper = (rs, ctx) -> {
        PlatformUser user = new PlatformUser();
        user.setId(UUID.fromString(rs.getString("id")));
        user.setName(rs.getString("name"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("pwd"));

        return user;
    };

    private final Jdbi db;
    public JdbiUserRepository(DataSource ds) {
        this.db = Jdbi.create(ds);
    }

    @Override
    public void insert(PlatformUser platformUser) {
        try (Handle conn = db.open()) {
            conn.createUpdate("insert into platform_user(id, name, login, pwd) values (:id::uuid, :name, :login, :pwd)")
                    .bind("id", platformUser.getId())
                    .bind("name", platformUser.getName())
                    .bind("login", platformUser.getLogin())
                    .bind("pwd", platformUser.getPassword())
                    .execute();
        }
    }

    @Override
    public Optional<PlatformUser> findById(UUID id) {
        try (Handle conn = db.open()) {
            return conn
                    .createQuery(SQL_GET_BY_ID)
                    .bind("id", id)
                    .map(userMapper)
                    .findFirst();
        }
    }

    @Override
    public Optional<PlatformUser> findByLogin(String login) {
        try (Handle conn = db.open()) {
            return conn
                .createQuery(SQL_GET_BY_LOGIN)
                .bind("login", login)
                .map(userMapper)
                .findFirst();
        }
    }

    @Override
    public void deleteById(UUID id) {
        try (Handle conn = db.open()) {
            conn
                    .createUpdate("delete from platform_user where id = :id::uuid")
                    .bind("id", id)
                    .execute();
        }

    }

    @Override
    public void update(PlatformUser platformUser) {
        try (Handle conn = db.open()) {
            conn.createUpdate("update platform_user set name = :name, login = :login, pwd = :pwd where id = :id::uuid")
                    .bind("id", platformUser.getId())
                    .bind("name", platformUser.getName())
                    .bind("login", platformUser.getLogin())
                    .bind("pwd", platformUser.getPassword())
                    .execute();
        }

    }
}

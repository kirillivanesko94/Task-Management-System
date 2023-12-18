package com.example.jira.test.rest;

import com.example.jira.db.task.*;
import com.example.jira.db.user.*;
import com.example.jira.test.db.*;
import com.example.jira.web.auth.*;
import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import org.springframework.security.crypto.password.*;

import java.util.*;

public class TestApi {
    private final TestRestClient restClient;
    private final DbHandler dbHandler;
    private final PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public TestApi(String baseRestApiUrl, DbHandler dbHandler, PasswordEncoder passwordEncoder) {
        this.restClient = new TestRestClient(baseRestApiUrl);
        this.dbHandler = dbHandler;
        this.passwordEncoder = passwordEncoder;
    }

    // REST API FUNCTIONS
    public RestResponse get(String url)  {
        return get(url, null);
    }

    public RestResponse get(String url, String token)  {
        return restClient.get(url, token);
    }

    public RestResponse post(String url, String body)  {
        return restClient.post(url, body, null);
    }

    public <T> RestResponse post(String url, T body) {
        return post(url, body, null);
    }

    public <T> RestResponse post(String url, T body, String token) {
        String strBody = null;
        try {
            if (body != null) {
                strBody = objectMapper.writeValueAsString(body);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return restClient.post(url, strBody, token);
    }
    public RestResponse delete(String url) {
        return delete(url, null);
    }

    public RestResponse delete(String url, String token) {
        return restClient.delete(url, token);
    }

    public RestResponse put(String url, String body) {
        return put(url, body, null);
    }

    public <T> RestResponse put(String url, T body) {
        return put(url, body, null);
    }

    public <T> RestResponse put(String url, T body, String token) {
        String strBody = null;
        try {
            if (body != null) {
                strBody = objectMapper.writeValueAsString(body);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return restClient.put(url, strBody, token);
    }

    public <T> T convertFromJson(String json, Class<T> contentClass) {
        try {
            return objectMapper.readValue(json, contentClass);
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    public <T> T convertFromJson(String json, TypeReference<T> typeRef) {
        try {
            return objectMapper.readValue(json, typeRef);
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    public String createUserAndReturnAuthToken(PlatformUser user) {
        userExists(toUserWithEncryptedPwd(user));
        RestResponse loginResp = post("auth/login", new LoginDto(user.getLogin(), user.getPassword()));
        TokenResponse token = convertFromJson(loginResp.getBody(), TokenResponse.class);

        return token.getAuthToken();
    }

    private PlatformUser toUserWithEncryptedPwd(PlatformUser user) {
        PlatformUser pu = PlatformUser.createNew(user.getName(), user.getLogin(), passwordEncoder.encode(user.getPassword()));
        pu.setId(user.getId());
        return pu;
    }

    // DB FUNCTIONS

    public void clearDb() {
        dbHandler.clearDb();
    }

    public void userExists(PlatformUser user) {
        usersExists(Collections.singletonList(user));
    }

    public void usersExists(Collection<PlatformUser> users) {
        dbHandler.usersExists(users);
    }

    public void taskExists(Task task) {
        tasksExists(Collections.singletonList(task));
    }

    public void tasksExists(Collection<Task> tasks) {
        dbHandler.tasksExists(tasks);
    }

}

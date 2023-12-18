package com.example.jira.web.auth;

import com.example.jira.test.*;
import com.example.jira.test.rest.*;
import org.junit.jupiter.api.*;

class AuthControllerTest extends BaseFuncTest {

    @Test
    void testRegistrationAndAuthorization() {
        String login = "kirill@ivanesko.xyz";
        String pwd = "tro_lo_lo";

        // Регистрируемся
        String request = String.format("""
        {
            "name": "Kirill",
            "login": "%s",
            "password": "%s"
        }
        """, login, pwd);

        RestResponse regResponse = api.post("auth/register", request);

        Assertions.assertEquals(200, regResponse.getCode());
        Assertions.assertNull(null, regResponse.getBody());

        // Проверяем что можем залогиниться с новым пользователем
        String loginReq = String.format("""
        {
            "login": "%s",
            "password": "%s"
        }
        """, login, pwd);
        RestResponse loginResp = api.post("auth/login", loginReq);
        TokenResponse token = api.convertFromJson(loginResp.getBody(), TokenResponse.class);

        Assertions.assertEquals(200, loginResp.getCode());
        Assertions.assertNotNull(token.getAuthToken());

        // Проверяем что полученный токен работает
        RestResponse meResp = api.get("me", token.getAuthToken());

        Assertions.assertEquals(200, meResp.getCode());
        Assertions.assertNotNull(meResp.getBody());
    }
}

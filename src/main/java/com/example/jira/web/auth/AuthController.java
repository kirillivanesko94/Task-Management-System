package com.example.jira.web.auth;

import com.example.jira.service.user.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<?> register (@RequestBody RegisterDto dto) {
        userService.registerNewUser(dto.getName(), dto.getLogin(), dto.getPassword());

        return ResponseEntity.status(200).build();
    }


    @PostMapping("login")
    public TokenResponse login(@RequestBody LoginDto dto) {
        String token = userService.login(dto.getLogin(), dto.getPassword());
        return new TokenResponse(token);
    }
}

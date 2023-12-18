package com.example.jira.web.me;

import com.example.jira.common.*;
import com.example.jira.db.user.*;
import com.example.jira.scurity.*;
import com.example.jira.service.user.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("me")
public class MeController {

    private final UserService userService;

    public MeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public MeInfoDto getMeInfo() {
        UUID currentUserId = SecurityUtils.getCurrentUserId();

        PlatformUser pu = userService
            .findById(currentUserId)
            .orElseThrow(() -> ObjectNotFoundException.user(currentUserId));

        return new MeInfoDto(pu);
    }
}

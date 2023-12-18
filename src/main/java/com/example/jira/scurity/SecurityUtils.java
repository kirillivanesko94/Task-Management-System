package com.example.jira.scurity;

import org.springframework.security.core.context.*;

import java.util.*;

public class SecurityUtils {

    public static UUID getCurrentUserId() {
        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (auth instanceof LoginAndIdPrincipal) {
            return ((LoginAndIdPrincipal) auth).getId();
        }

        throw new RuntimeException("Unexpected auth principal");
    }
}

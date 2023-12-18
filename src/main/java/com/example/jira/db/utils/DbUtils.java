package com.example.jira.db.utils;

import org.springframework.util.*;

import java.util.*;

public class DbUtils {

    public static UUID stringToUUID(String input) {
        if (ObjectUtils.isEmpty(input)) {
            return null;
        }

        return UUID.fromString(input);
    }
}

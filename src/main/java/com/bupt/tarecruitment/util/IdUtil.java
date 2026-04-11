package com.bupt.tarecruitment.util;

import java.util.UUID;

public class IdUtil {
    private IdUtil() {
    }

    public static String generateId(String prefix) {
        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return prefix + "-" + randomPart;
    }
}

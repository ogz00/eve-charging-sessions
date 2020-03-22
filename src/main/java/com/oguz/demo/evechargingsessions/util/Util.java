package com.oguz.demo.evechargingsessions.util;

import java.time.LocalDateTime;
import java.util.UUID;

public class Util {

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    public static String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }
}

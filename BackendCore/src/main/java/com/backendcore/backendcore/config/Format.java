package com.backendcore.backendcore.config;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class Format {
    public static final DateTimeFormatter BIRTHDAY_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    public static String formatTimestamp(Timestamp timestamp) {
        if(timestamp == null) return "";
        return timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}

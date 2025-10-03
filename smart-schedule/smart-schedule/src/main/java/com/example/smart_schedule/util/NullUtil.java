package com.example.smart_schedule.util;

public class NullUtil {
    public static <T> T nullCheck(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static String getOrDefault(String value, String defaultValue) {
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    public static String safeTrim(String str) {
        return str == null ? "" : str.trim();
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

}

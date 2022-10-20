package com.ruckusexam.utils;

public class ParamCheckUtil {
    public static boolean isEmpty(String str) {
        if (str == null || "null".equals(str) || str.trim().isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Integer str) {
        if (str == null) {
            return true;
        }
        return false;
    }
}

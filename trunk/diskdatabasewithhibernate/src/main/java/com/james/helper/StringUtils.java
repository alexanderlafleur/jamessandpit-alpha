package com.james.helper;

public class StringUtils {

    public static String trim(String s) {
        if (s != null) {
            int length = s.length();

            if (length > 255) {
                length = 255;
            }

            return s.substring(0, length);
        } else {
            return s;
        }
    }
}

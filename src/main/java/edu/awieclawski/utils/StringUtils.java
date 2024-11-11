package edu.awieclawski.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

    public static String afterTheLastPatternBySubstring(String input, String pattern) {
        int index = input.lastIndexOf(pattern);
        return index >= 0 ? input.substring(index + pattern.length()) : "";
    }
}

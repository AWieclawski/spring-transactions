package edu.awieclawski.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InstantUtils {

    public static String getCurrentTimestampAsString(DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.format(Instant.now());
    }
}

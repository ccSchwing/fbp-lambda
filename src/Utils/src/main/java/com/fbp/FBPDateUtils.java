package com.fbp;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FBPDateUtils {
       public static String getCurrentEstAsIsoString() {
        ZonedDateTime estTime = ZonedDateTime.now(ZoneId.of("America/New_York"));
        return estTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        // Returns: "2026-03-05T12:31:47-05:00"
    } 
}

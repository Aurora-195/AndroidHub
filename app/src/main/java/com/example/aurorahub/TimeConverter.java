package com.example.aurorahub;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeConverter {
    public static String convertToISO8601UTC(String time) {
        // Parse the time
        LocalTime localTime = LocalTime.parse(time);
        LocalDate date = LocalDate.now(ZoneId.of("America/Los_Angeles"));
        // Combine LocalDate and LocalTime to get LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.of(date, localTime);

        ZoneId pacificZone = ZoneId.of("America/Los_Angeles");

        // Convert LocalDateTime to ZonedDateTime in Pacific Time
        ZonedDateTime zonedPDT = localDateTime.atZone(pacificZone);

        // Convert Pacific Time to UTC
        ZonedDateTime zonedUTC = zonedPDT.withZoneSameInstant(ZoneId.of("UTC"));

        // Format to ISO 8601 format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return zonedUTC.format(formatter);
    }
}

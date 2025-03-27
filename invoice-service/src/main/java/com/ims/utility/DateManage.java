package com.ims.utility;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class DateManage {
    public static void main(String[] args) {
        System.out.println(Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).toLocalDateTime());
        System.out.println("Date: "+ LocalDate.now());
        System.out.println(("Time "+ LocalTime.now().withSecond(0).withNano(0)));
    }
}

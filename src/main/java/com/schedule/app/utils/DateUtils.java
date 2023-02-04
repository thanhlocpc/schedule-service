package com.schedule.app.utils;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 * @author ThanhLoc
 * @created 2/4/2023
 */
public class DateUtils {

    public static String dateToString(Date date) {
        return (date.getDate() < 10 ? "0" + date.getDate() : date.getDate()) + "/" + (date.getMonth() + 1) + "/" + (date.getYear()+1900);
    }
}

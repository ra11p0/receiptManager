package com.ra11p0.Utils;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    public static int getDay(Date date){ return calendar(date).get(Calendar.DAY_OF_MONTH); }
    public static int getMonth(Date date){ return calendar(date).get(Calendar.MONTH); }
    public static int getYear(Date date){ return calendar(date).get(Calendar.YEAR); }
    public static boolean equals(Date date1, Date date2){ return getDay(date1) == getDay(date2) & getMonth(date1) == getMonth(date2) & getYear(date1) == getYear(date2); }
    private static Calendar calendar(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        calendar.setTimeInMillis(date.getTime());
        return calendar;
    }
}

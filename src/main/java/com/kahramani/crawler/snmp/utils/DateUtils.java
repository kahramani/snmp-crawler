package com.kahramani.crawler.snmp.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by kahramani on 11/22/2016.
 */
public class DateUtils {

    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyyMMddHHmmss";

    /**
     * to get date time in long type - Format: DEFAULT_DATE_TIME_FORMAT
     * @return a long value
     */
    public static Long getDateTimeAsLong() {
        DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
        Calendar cal = Calendar.getInstance();
        String formattedTime = dateFormat.format(cal.getTime());
        return Long.valueOf(formattedTime);
    }
}

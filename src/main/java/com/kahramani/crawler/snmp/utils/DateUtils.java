package com.kahramani.crawler.snmp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by kahramani on 11/22/2016.
 */
public class DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyyMMddHHmmss";

    public static Long getDateTimeAsLong() {
        DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
        Calendar cal = Calendar.getInstance();
        String formattedTime = dateFormat.format(cal.getTime());
        return Long.valueOf(formattedTime);
    }
}

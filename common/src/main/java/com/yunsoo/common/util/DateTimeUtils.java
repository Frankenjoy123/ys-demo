package com.yunsoo.common.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Created by:   Lijian
 * Created on:   2015/3/5
 * Descriptions:
 */
public final class DateTimeUtils {

    /**
     * server local datetime string
     *
     * @param dateTime
     * @return yyyy-MM-dd'T'HH:mm:ss.SSSZZ  example: 2015-05-06T15:35:07.902+08:00
     */
    public static String toString(DateTime dateTime) {
        return dateTime == null ? null : dateTime.toString("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
    }

    /**
     * UTC datetime string
     *
     * @param dateTime
     * @return yyyy-MM-dd'T'HH:mm:ss.SSS'Z'  example: 2015-05-06T07:35:07.902Z
     */
    public static String toUTCString(DateTime dateTime) {
        return dateTime == null ? null : dateTime.toDateTime(DateTimeZone.UTC).toString();//"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    }

    /**
     * UTC datetime string for database
     *
     * @param dateTime
     * @return yyyy-MM-dd HH:mm:ss.SSS  example: 2015-05-06 07:35:07.902
     */
    public static String toDBString(DateTime dateTime) {
        return dateTime == null ? null : dateTime.toDateTime(DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");
    }

    public static String toDateString(DateTime dateTime) {
        return dateTime == null ? null : dateTime.toLocalDate().toString("yyyy-MM-dd");
    }

    public static DateTime parse(String str) {
        return str == null ? null : DateTime.parse(str);
    }

}

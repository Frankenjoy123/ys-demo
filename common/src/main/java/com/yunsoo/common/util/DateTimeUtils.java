package com.yunsoo.common.util;

import org.joda.time.DateTime;

/**
 * Created by:   Lijian
 * Created on:   2015/3/5
 * Descriptions:
 */
public final class DateTimeUtils {

    public static String toString(DateTime dateTime) {
        return dateTime == null ? null : dateTime.toString("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
    }

    public static String toDateString(DateTime dateTime) {
        return dateTime == null ? null : dateTime.toLocalDate().toString();
    }

    public static DateTime parse(String str) {
        return str == null ? null : DateTime.parse(str);
    }

}

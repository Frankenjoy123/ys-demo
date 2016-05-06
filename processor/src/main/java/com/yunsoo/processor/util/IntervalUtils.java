package com.yunsoo.processor.util;

import org.joda.time.DateTime;

/**
 * Created by:   Lijian
 * Created on:   2016-04-22
 * Descriptions:
 */
public final class IntervalUtils {

    /**
     * @param startDateTime start point of the DateTime
     * @param interval      String must match the regular expression: \d+[M|w|d|h|m|s]
     * @return result datetime
     */
    public static DateTime plusInterval(DateTime startDateTime, String interval) {
        if (startDateTime == null || interval == null) {
            return null;
        }
        interval = interval.trim();
        if (interval.length() < 2) {
            return null;
        }

        char unit = interval.charAt(interval.length() - 1);
        int numeral;
        try {
            numeral = Integer.parseInt(interval.substring(0, interval.length() - 1));
        } catch (NumberFormatException e) {
            return null;
        }

        if (numeral <= 0) {
            return null;
        }

        switch (unit) {
            case 's':
                return startDateTime.plusSeconds(numeral);
            case 'm':
                return startDateTime.plusMinutes(numeral);
            case 'h':
                return startDateTime.plusHours(numeral);
            case 'd':
                return startDateTime.plusDays(numeral);
            case 'w':
                return startDateTime.plusWeeks(numeral);
            case 'M':
                return startDateTime.plusMonths(numeral);
            default:
                return null;
        }
    }
}
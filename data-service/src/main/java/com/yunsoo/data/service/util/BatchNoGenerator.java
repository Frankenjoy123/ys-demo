package com.yunsoo.data.service.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by:   Lijian
 * Created on:   2016-03-21
 * Descriptions:
 */
public final class BatchNoGenerator {

    private BatchNoGenerator() {
    }

    public static String getNew() {
        return String.format("%s%05d%03d", getDate(), getSeconds(), getRandom());
    }

    private static String getDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyMMdd");
        df.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        return df.format(new Date());
    }

    private static int getSeconds() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+00:00"));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return hour * 3600 + minute * 60 + second;
    }

    private static int getRandom() {
        return new Random().nextInt(1000);
    }
}

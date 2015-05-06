package com.yunsoo.common.util;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Created by:   Lijian
 * Created on:   2015/3/5
 * Descriptions:
 */
public class DateTimeUtilsTest {

    @Test
    public void test_JodaDateTime() {
        DateTime now = DateTime.now();
        assert now.isEqual(DateTimeUtils.parse(DateTimeUtils.toString(now)));
        assert now.isEqual(DateTimeUtils.parse(DateTimeUtils.toUTCString(now)));
        System.out.println(now);

        System.out.println("DateTimeUtils.toString     :\t" + DateTimeUtils.toString(now));
        System.out.println("DateTimeUtils.toUTCString  :\t" + DateTimeUtils.toUTCString(now));
        System.out.println("DateTimeUtils.toDBString   :\t" + DateTimeUtils.toDBString(now));
        System.out.println("DateTimeUtils.toDateString :\t" + DateTimeUtils.toDateString(now));

    }
}

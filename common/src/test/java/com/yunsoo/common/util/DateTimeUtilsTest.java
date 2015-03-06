package com.yunsoo.common.util;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
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
        System.out.println(DateTimeUtils.toDateString(now));
        System.out.println(DateTime.now().toString());
//        System.out.println(DateTime.now().toString("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
//        System.out.println();
//        System.out.println(DateTime.parse("2015-03-04T07:15:10.123").toString());
//        System.out.println(DateTime.parse("2015-03-04T07:15:10.123Z").toString());
//        System.out.println(DateTime.parse("2015-03-04T07:15:10.123+01:00").toString());
//        System.out.println(DateTime.parse("2015-03-04T07:15:10.123+08:00").toString());
//        System.out.println();
//        System.out.println(DateTime.parse("2015-03-04T07:15:10.123").toString("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
//        System.out.println(DateTime.parse("2015-03-04T07:15:10.123Z").toString("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
//        System.out.println(DateTime.parse("2015-03-04T07:15:10.123-01:00").toString("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
//        System.out.println(DateTime.parse("2015-03-04T07:15:10.123+08:00").toString("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
    }
}

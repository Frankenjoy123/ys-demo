package com.yunsoo.common.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Random;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/22
 * Descriptions:
 */
public final class RandomUtils {

    public static final char[] CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public static final char[] NUMERIC_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static final char[] ALPHABET_CHARS = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public static String generateString(int length) {
        return generateString(length, CHARS);
    }

    public static String generateString(int length, char[] source) {
        if (source == null || source.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(source[random.nextInt(source.length)]);
        }
        return sb.toString();
    }

    public static String  generateStringWithDate() {
        return DateTime.now().toDateTime(DateTimeZone.UTC).toString("yyMMddHHmmssSSS") + generateString(3, RandomUtils.NUMERIC_CHARS);
    }
}

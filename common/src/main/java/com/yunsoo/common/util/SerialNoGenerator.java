package com.yunsoo.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by:   Lijian
 * Created on:   2016-12-15
 * Descriptions:
 */
public class SerialNoGenerator {

    private static Pattern REGEXP = Pattern.compile("^(?<prefix>[\\w\\d\\-]*)\\[(?<start>\\d+)\\-(?<end>\\d+)\\](?<suffix>[\\w\\d\\-]*)$");

    String pattern;
    String prefix;
    String suffix;
    int start;
    int end;
    int serialLength;
    int totalLength;
    int totalCount;

    /**
     * @param serialNoPattern String: prefix[001-999]suffix
     */
    public SerialNoGenerator(String serialNoPattern) {
        this.pattern = serialNoPattern;
        Matcher matcher = REGEXP.matcher(serialNoPattern);
        if (!matcher.find()) {
            throw new IllegalArgumentException("pattern invalid");
        }
        prefix = matcher.group("prefix");
        suffix = matcher.group("suffix");
        String startStr = matcher.group("start");
        String endStr = matcher.group("end");
        if (startStr.length() != endStr.length()) {
            throw new IllegalArgumentException("pattern invalid");
        }
        start = Integer.parseInt(startStr);
        end = Integer.parseInt(endStr);
        if (start > end) {
            throw new IllegalArgumentException("pattern invalid");
        }
        serialLength = startStr.length();
        totalLength = prefix.length() + serialLength + suffix.length();
        totalCount = end - start + 1;
    }

    public SerialNoGenerator(String prefix, String suffix, int serialLength, int start, int end) {
        if (serialLength < 0 || start < 0 || start > end || end > Math.pow(10, serialLength) - 1) {
            throw new IllegalArgumentException();
        }
        this.prefix = prefix == null ? "" : prefix;
        this.suffix = suffix == null ? "" : suffix;
        this.serialLength = serialLength;
        this.start = start;
        this.end = end;
        this.totalLength = this.prefix.length() + serialLength + this.suffix.length();
        this.totalCount = end - start + 1;
        this.pattern = String.format("%s[%0" + serialLength + "d-%0" + serialLength + "d]%s", this.prefix, this.start, this.end, this.suffix);
    }

    public SerialNoGenerator(String prefix, String suffix, int serialLength, int start) {
        this(prefix, suffix, serialLength, start, (int) Math.pow(10, serialLength) - 1);
    }

    public String getPattern() {
        return pattern;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getSerialLength() {
        return serialLength;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public int getTotalCount() {
        return totalCount;
    }

    /**
     * @param index start from 0
     * @return serialNo on the given index
     */
    public String getSerialNo(int index) {
        int serial = start + index;
        if (index < 0 || serial > end) {
            throw new IndexOutOfBoundsException("index out of serial range");
        }
        return String.format("%s%0" + serialLength + "d%s", prefix, serial, suffix);
    }

    /**
     * @param index start from 0
     * @param count serial count
     * @return sub serialNoPattern
     */
    public String getSubSerialNoPattern(int index, int count) {
        int newStart = start + index;
        int newEnd = newStart + count - 1;
        if (newStart < start || newEnd > end || newEnd < newStart) {
            throw new IndexOutOfBoundsException("sub pattern out of serial range");
        }
        return String.format("%s[%0" + serialLength + "d-%0" + serialLength + "d]%s", prefix, newStart, newEnd, suffix);
    }

}

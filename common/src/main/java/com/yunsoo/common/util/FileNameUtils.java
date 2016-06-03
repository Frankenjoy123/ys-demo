package com.yunsoo.common.util;

/**
 * Created by:   Lijian
 * Created on:   2016-06-02
 * Descriptions:
 */
public final class FileNameUtils {

    public static String shorten(String fileName, int limit) {
        if (fileName == null || fileName.length() == 0 || limit <= 0 || fileName.length() <= limit) {
            return fileName;
        }
        int length = fileName.length();
        int dotIndex = fileName.lastIndexOf(".");
        return dotIndex > 0 && length - dotIndex < limit
                ? fileName.substring(0, limit - length + dotIndex) + fileName.substring(dotIndex)
                : fileName.substring(0, limit);
    }
}

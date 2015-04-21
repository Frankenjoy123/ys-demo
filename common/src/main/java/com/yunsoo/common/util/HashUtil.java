package com.yunsoo.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/21
 * Descriptions:
 */
public class HashUtil {

    public static String hash(String src) {
        MessageDigest mdInstance;
        try {
            mdInstance = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash algorithm not found", e);
        }
        mdInstance.update(src.getBytes());
        byte[] resultBytes = mdInstance.digest();
        return toHexString(resultBytes);
    }

    private static String toHexString(byte[] bytes) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char chars[] = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) {
            chars[index++] = hexDigits[b >> 4 & 0xf];
            chars[index++] = hexDigits[b & 0xf];
        }
        return new String(chars);
    }
}

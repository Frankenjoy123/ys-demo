package com.yunsoo.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/21
 * Descriptions:
 */
public class HashUtils {

    public static String md5(String src) {
        return hash(src, "MD5");
    }

    public static String sha1(String src) {
        return hash(src, "SHA-1");
    }

    public static String sha256(String src) {
        return hash(src, "SHA-256");
    }

    public static String hash(String src, String algorithm) {
        MessageDigest mdInstance;
        String encoding = "UTF-8";
        try {
            mdInstance = MessageDigest.getInstance(algorithm);
            mdInstance.update(src.getBytes(encoding));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("hash algorithm not found: " + algorithm);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("unsupported encoding: " + encoding);
        }
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

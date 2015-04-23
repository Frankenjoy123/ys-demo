package com.yunsoo.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/21
 * Descriptions:
 */
public class HashUtils {

    public static byte[] md5(byte[] src) {
        return hash(src, "MD5");
    }

    public static byte[] sha1(byte[] src) {
        return hash(src, "SHA-1");
    }

    public static byte[] sha256(byte[] src) {
        return hash(src, "SHA-256");
    }


    public static byte[] md5(String src) {
        return hash(src, "MD5");
    }

    public static byte[] sha1(String src) {
        return hash(src, "SHA-1");
    }

    public static byte[] sha256(String src) {
        return hash(src, "SHA-256");
    }


    public static String md5HexString(String src) {
        return toHexString(md5(src));
    }

    public static String sha1HexString(String src) {
        return toHexString(sha1(src));
    }

    public static String sha256HexString(String src) {
        return toHexString(sha256(src));
    }


    public static byte[] hash(String src, String algorithm) {
        return hash(src.getBytes(StandardCharsets.UTF_8), algorithm);
    }

    public static byte[] hash(byte[] src, String algorithm) {
        MessageDigest mdInstance;
        try {
            mdInstance = MessageDigest.getInstance(algorithm);
            mdInstance.update(src);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("hash algorithm not found: " + algorithm);
        }
        return mdInstance.digest();
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

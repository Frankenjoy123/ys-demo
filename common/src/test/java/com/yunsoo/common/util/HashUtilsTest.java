package com.yunsoo.common.util;

import org.junit.Test;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/21
 * Descriptions:
 */
public class HashUtilsTest {

    @Test
    public void test_hash() {
        String salt = RandomUtils.generateString(8);
        String src = "admin" + salt;
        System.out.println(salt);
        System.out.println(HashUtils.md5HexString(src));
        System.out.println(HashUtils.sha1HexString(src));
        System.out.println(HashUtils.sha256HexString(src));
        System.out.println(HashUtils.sha1(src).length);
        System.out.println(HashUtils.sha256(src).length);

    }

    @Test
    public void generatePassword() {
        String rawPassword = "123456";
        String hashSalt = RandomUtils.generateString(8);
        String password = HashUtils.sha1HexString(rawPassword + hashSalt);
        System.out.println("rawPassword:\t" + rawPassword);
        System.out.println("hashSalt:   \t" + hashSalt);
        System.out.println("password:   \t" + password);
    }

}

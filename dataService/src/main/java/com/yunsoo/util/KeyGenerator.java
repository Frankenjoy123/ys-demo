package com.yunsoo.util;


import java.util.UUID;

public final class KeyGenerator {

    public static String newKey() {
        String key;
        String uuid = UUID.randomUUID().toString();
        key = uuid;
        System.out.println(uuid);

        return key;

    }
}

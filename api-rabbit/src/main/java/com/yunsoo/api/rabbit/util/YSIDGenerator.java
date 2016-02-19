package com.yunsoo.api.rabbit.util;

import java.util.UUID;

/**
 * Created by:   Lijian
 * Created on:   2016-02-19
 * Descriptions:
 */
public final class YSIDGenerator {

    public static String getNew() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static boolean validate(String ysid) {
        return ysid != null && ysid.length() == 32;
    }
}

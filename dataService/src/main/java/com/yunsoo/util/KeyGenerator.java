package com.yunsoo.util;


import org.apache.commons.codec.binary.Base64;

import java.nio.ByteBuffer;
import java.util.UUID;


public final class KeyGenerator {

    public static String newKey() {
        UUID uuid = UUID.randomUUID();
        byte[] keyArr = ByteBuffer.wrap(new byte[16])
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits())
                .array();
        return Base64.encodeBase64URLSafeString(keyArr);
    }

}

package com.yunsoo.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by:   yan
 * Created on:   11/26/2015
 * Descriptions:
 */
public final class ObjectUtils {

    private static ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        return obj == null ? null : MAPPER.writeValueAsString(obj);
    }

    public static <T> T toObject(String json, Class<T> valueType) throws IOException {
        return json == null || json.length() == 0 ? null : MAPPER.readValue(json, valueType);
    }
}

package com.yunsoo.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2016-03-29
 * Descriptions:
 */
public final class StringFormatter {

    public static String formatMap(Object... objects) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 1; i < objects.length; i += 2) {
            if (objects[i - 1] != null) {
                String name = objects[i - 1].toString();
                Object value = objects[i];
                sb.append(name).append(": ").append(value);
                if (i < objects.length - 2) {
                    sb.append(", ");
                }
            }
        }
        return sb.append("]").toString();
    }

    public static String formatMap(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("[");
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i) != null) {
                String name = keys.get(i);
                Object value = map.get(name);
                sb.append(name).append(": ").append(value);
                if (i < keys.size() - 1) {
                    sb.append(", ");
                }
            }
        }
        return sb.append("]").toString();
    }
}

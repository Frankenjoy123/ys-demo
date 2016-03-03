package com.yunsoo.data.service.util;

import java.lang.reflect.Field;

/**
 * Created by:   Lijian
 * Created on:   2016-03-03
 * Descriptions:
 */
public final class EntityUtils {

    private EntityUtils() {
    }

    public static <T> void patchUpdate(T entity, T fromEntity) {
        if (entity != null && fromEntity != null) {
            Field[] fields = entity.getClass().getDeclaredFields();
            try {
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(fromEntity);
                    if (value != null) {
                        field.set(entity, value);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

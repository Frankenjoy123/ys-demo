package com.yunsoo.api.rabbit.cache;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * Created by:   yan
 * Created on:   7/24/2015
 * Descriptions:
 */
public class ObjectKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder keyName = new StringBuilder()
                .append("object:")
                .append(method.getReturnType().getName())
                .append(".");
        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {
                keyName.append(params[i].toString());
            }
            if (i < params.length - 1) {
                keyName.append("|");
            }
        }
        return keyName.toString();
    }

    public static Object generate(String objectType, Object... params) {
        StringBuilder keyName = new StringBuilder()
                .append("object:")
                .append(objectType)
                .append(".");
        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {
                keyName.append(params[i].toString());
            }
            if (i < params.length - 1) {
                keyName.append("|");
            }
        }
        return keyName.toString();
    }

}

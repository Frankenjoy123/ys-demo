package com.yunsoo.api.rabbit.cache;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * Created by:   yan
 * Created on:   7/24/2015
 * Descriptions:
 */
public class CustomKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params){
        StringBuilder keyName = new StringBuilder();
        keyName.append(target.getClass().getName());
        keyName.append(".");
        keyName.append(method.getName());
        keyName.append(".");
        for(Object para : params){
            if(para == null)
                continue;
            keyName.append(para.toString());
            keyName.append(".");
        }
        return keyName.toString();
    }

    public static Object generate(String targetName, Object... params){
        StringBuilder keyName = new StringBuilder();
        keyName.append(targetName);
        keyName.append(".");
        for(Object para : params){
            if(para == null)
                continue;
            keyName.append(para.toString());
            keyName.append(".");
        }
        return keyName.toString();
    }

    public static Object getKey(String targetName, Object... params){
        StringBuilder keyName = new StringBuilder();
        keyName.append(targetName);
        keyName.append(".");
        for(Object para : params){
            if(para == null)
                continue;
            keyName.append(para.toString());
            keyName.append(".");
        }
        return keyName.toString();
    }

    public static Object getKey(String className, String methodName, Object... params){
        StringBuilder keyName = new StringBuilder();
        keyName.append(className);
        keyName.append(".");
        keyName.append(methodName);
        keyName.append(".");
        for(Object para : params){
            if(para == null)
                continue;
            keyName.append(para.toString());
            keyName.append(".");
        }
        return keyName.toString();
    }
}

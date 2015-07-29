package com.yunsoo.api.cache;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * Created by yan on 7/24/2015.
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
}

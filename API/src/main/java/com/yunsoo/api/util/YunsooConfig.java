package com.yunsoo.api.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by:   Lijian
 * Created on:   2015/2/28
 * Descriptions:
 */
public final class YunsooConfig {

    private static final String PROPERTY_YUNSOO_DATAAPI_BASEURI = "yunsoo.dataapi.baseuri";
    private static final Properties properties = new Properties();

    static {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            properties.load(loader.getResourceAsStream("yunsoo.properties"));
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static String getSetting(String key) {
        return properties.getProperty(key);
    }

    public static String getDataAPIBaseUri(){
        return  getSetting(PROPERTY_YUNSOO_DATAAPI_BASEURI);
    }

}

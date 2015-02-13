package com.yunsoo.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Zhe on 2015/1/27.
 */
public final class YunsooConfig {

    private static final String PROPERTY_YUNSOO_MESSAGE_DELETE_STATUS_ID = "yunsoo.message.delete_status_id";
    private static final String PROPERTY_YUNSOO_MESSAGE_APPROVED_STATUS_ID = "yunsoo.message.approved_status_id";
    private static final String PROPERTY_YUNSOO_MESSAGE_CREATED_STATUS_ID = "yunsoo.message.created_status_id";
    private static final String PROPERTY_YUNSOO_PRODUCT_KEY_BATCH_S3_BUCKET_NAME = "yunsoo.product_key_batch.s3_bucket_name";
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

    public static Integer getMessageDeleteStatus() {
        return Integer.parseInt(getSetting(PROPERTY_YUNSOO_MESSAGE_DELETE_STATUS_ID));
    }

    public static Integer getMessageCreatedStatus() {
        return Integer.parseInt(getSetting(PROPERTY_YUNSOO_MESSAGE_CREATED_STATUS_ID));
    }

    public static Integer getMessageApprovedStatus() {
        return Integer.parseInt(getSetting(PROPERTY_YUNSOO_MESSAGE_APPROVED_STATUS_ID));
    }

    public static String getProductKeyBatchS3bucketName() {
        return getSetting(PROPERTY_YUNSOO_PRODUCT_KEY_BATCH_S3_BUCKET_NAME);
    }
}

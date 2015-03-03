package com.yunsoo.util;

import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Zhe on 2015/1/27.
 */
//@PropertySource("classpath:yunsoo.properties")
public final class YunsooConfig {

    private static final String PROPERTY_YUNSOO_MESSAGE_DELETE_STATUS_ID = "yunsoo.message.delete_status_id";
    private static final String PROPERTY_YUNSOO_MESSAGE_APPROVED_STATUS_ID = "yunsoo.message.approved_status_id";
    private static final String PROPERTY_YUNSOO_MESSAGE_CREATED_STATUS_ID = "yunsoo.message.created_status_id";
    private static final String PROPERTY_YUNSOO_USER_CREATED_STATUS = "yunsoo.user.created_status_id";
    private static final String PROPERTY_YUNSOO_USER_DELETED_STATUS = "yunsoo.user.delete_status_id";
    private static final String PROPERTY_YUNSOO_PRODUCT_KEY_BATCH_S3_BUCKET_NAME = "yunsoo.product_key_batch.s3_bucket_name";
    private static final String PROPERTY_YUNSOO_BASE_BUCKET = "yunsoo.s3.basebucket";
    private static final String PROPERTY_YUNSOO_USER_BASEURL = "yunsoo.s3.userbaseurl";

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

    public static Integer getUserCreatedStatus() {
        return Integer.parseInt(getSetting(PROPERTY_YUNSOO_USER_CREATED_STATUS));
    }

    public static Integer getUserDeletedStatus() {
        return Integer.parseInt(getSetting(PROPERTY_YUNSOO_USER_DELETED_STATUS));
    }

    public static String getProductKeyBatchS3bucketName() {
        return getSetting(PROPERTY_YUNSOO_PRODUCT_KEY_BATCH_S3_BUCKET_NAME);
    }

    public static String getBaseBucket() {
        return getSetting(PROPERTY_YUNSOO_BASE_BUCKET);
    }

    public static String getUserBaseURL() {
        return getSetting(PROPERTY_YUNSOO_USER_BASEURL);
    }
}

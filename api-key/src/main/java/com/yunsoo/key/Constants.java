package com.yunsoo.key;

import java.util.Arrays;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-19
 * Descriptions:
 */
public class Constants {
    private Constants() {
    }

    public static class KeyBatchStatus {
        private KeyBatchStatus() {
        }

        public static final String NEW = "new";
        public static final String CREATING = "creating";
        public static final String AVAILABLE = "available";
        public static final String DELETED = "deleted";
    }

    public static class ProductStatus {
        private ProductStatus() {
        }

        public static final String CREATED = "created";
        public static final String ACTIVATED = "activated";
        public static final String RECALLED = "recalled";
        public static final String DELETED = "deleted";
        public static final List<String> ALL = Arrays.asList(CREATED, ACTIVATED, RECALLED, DELETED);
    }

    public static class ProductKeyType {
        private ProductKeyType() {
        }

        public static final String PACKAGE = "package";
        public static final String QR_PUBLIC = "qr_public";
        public static final String QR_SECURE = "qr_secure";
        public static final String RFID = "rfid";
    }

}

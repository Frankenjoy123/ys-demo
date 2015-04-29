package com.yunsoo.common.data;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/27
 * Descriptions:
 */
public class LookupCodes {
    private LookupCodes() {
    }

    public static class ProductKeyBatchStatus {
        private ProductKeyBatchStatus() {
        }

        public static final String CREATING = "creating";
        public static final String AVAILABLE = "available";
        public static final String DOWNLOADED = "downloaded";
        public static final String DELETED = "deleted";
    }

    public static class ProductStatus {
        private ProductStatus() {
        }

        public static final String NEW = "new";
        public static final String ACTIVATED = "activated";
        public static final String RECALLED = "recalled";
        public static final String DELETED = "deleted";
    }

    public static class ProductKeyType {
        private ProductKeyType() {
        }

    }

    public static class AccountStatus {
        private AccountStatus() {
        }

        public static final String ACTIVATED = "activated";
        public static final String DEACTIVATED = "deactivated";
    }

    public static class MessageStatus {
        private MessageStatus() {
        }

        public static final String CREATED = "created";
    }
}

package com.yunsoo.common.data;

import java.util.ArrayList;
import java.util.List;

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

        public static final String NEW = "new";
        public static final String CREATING = "creating";
        public static final String AVAILABLE = "available";
        public static final String DOWNLOADED = "downloaded";
        public static final String DELETED = "deleted";
    }

    public static class ProductStatus {
        private ProductStatus() {
        }

        public static final String CREATED = "created";
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
        public static final String DELETED = "deleted";
        public static final String APPROVED = "approved";
    }

    public static class ProductBaseStatus {

        private ProductBaseStatus() {
        }

        public static final String CREATED = "created";
        public static final String DELETED = "deleted";
        public static final String FROZEN = "frozen";
        public static final String APPROVED = "approved";
        public static final List<String> CUSTOMER_INVISIBLE_STATUS;

        static {
            CUSTOMER_INVISIBLE_STATUS = new ArrayList<>();
            CUSTOMER_INVISIBLE_STATUS.add(FROZEN);
            CUSTOMER_INVISIBLE_STATUS.add(DELETED);
        }

    }

    public static class ProductKeyTransactionStatus {
        private ProductKeyTransactionStatus() {
        }

        public static final String CREATED = "created";
        public static final String COMMITTED = "committed";
        public static final String ROLLBACKED = "rollbacked";
    }
}

package com.yunsoo.common.data;

import java.util.Arrays;
import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/27
 * Descriptions:
 */
public class LookupCodes {
    private LookupCodes() {
    }

    public static class ApplicationStatus {
        private ApplicationStatus() {
        }

        public static final String CREATED = "created";
        public static final String ACTIVE = "active";
        public static final String UPDATABLE = "updatable";
        public static final String FORCEUPDATABLE = "force updatable";
        public static final String INACTIVE = "inactive";
    }


    public static class ProductKeyBatchStatus {
        private ProductKeyBatchStatus() {
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
    }

    public static class ProductKeyType {
        private ProductKeyType() {
        }

    }

    public static class AccountStatus {
        private AccountStatus() {
        }

        public static final String CREATED = "created";
        public static final String AVAILABLE = "available";
    }

    public static class MessageStatus {
        private MessageStatus() {
        }

        public static final String CREATED = "created";
        public static final String DELETED = "deleted";
        public static final String APPROVED = "approved";
        public static final String SUCCESSFULLY_PUSHED = "successfully pushed";
    }

    public static class MessageType {
        private MessageType() {
        }

        public static final String BUSINESS = "business";
    }

    public static class ProductBaseStatus {

        private ProductBaseStatus() {
        }

        public static final String CREATED = "created";
        public static final String ACTIVATED = "activated";
        public static final String DEACTIVATED = "deactivated";
    }

    public static class ProductBaseVersionsStatus {

        private ProductBaseVersionsStatus() {
        }

        public static final String DRAFT = "draft";
        public static final String SUBMITTED = "submitted";
        public static final String REJECTED = "rejected";
        public static final String ACTIVATED = "activated";
        public static final String ARCHIVED = "archived";
        public static final List<String> EDITABLE_STATUS = Arrays.asList("draft", "rejected", "submitted");
    }

    public static class ProductBaseVersionsApprovalStatus {

        private ProductBaseVersionsApprovalStatus() {
        }

        public static final String APPROVED = "approved";
        public static final String REJECTED = "rejected";
    }


    public static class ProductKeyTransactionStatus {
        private ProductKeyTransactionStatus() {
        }

        public static final String CREATED = "created";
        public static final String COMMITTED = "committed";
        public static final String ROLLBACK = "rollback";
    }

    public static class DeviceStatus {
        private DeviceStatus() {
        }

        public static final String ACTIVATED = "activated";
    }
}

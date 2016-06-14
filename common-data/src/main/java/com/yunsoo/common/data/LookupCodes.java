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
        public static final String FORCE_UPDATE = "force_update";
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

        public static final String PACKAGE = "package";
        public static final String QR_PUBLIC = "qr_public";
        public static final String QR_SECURE = "qr_secure";
        public static final String RFID = "rfid";
    }

    public static class AccountStatus {
        private AccountStatus() {
        }

        public static final String CREATED = "created";
        public static final String AVAILABLE = "available";
        public static final String DISABLED = "disabled";
    }

    public static class MessageStatus {
        private MessageStatus() {
        }

        public static final String CREATED = "created";
        public static final String DELETED = "deleted";
        public static final String APPROVED = "approved";
        public static final String PUSHED = "pushed";
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

    public static class UserPointTransactionStatus {
        private UserPointTransactionStatus() {
        }

        public static final String CREATED = "created";
        public static final String COMMITTED = "committed";
        public static final String ROLLBACK = "rollback";
    }

    public static class UserPointTransactionType {
        private UserPointTransactionType() {
        }

        public static final String SIGN_IN_REWARDS = "sign_in_rewards";
    }

    public static class UserStatus {
        private UserStatus() {
        }

        public static final String ENABLED = "enabled";
        public static final String DISABLED = "disabled";
    }

    public static class OrgAgencyStatus {
        private OrgAgencyStatus() {
        }

        public static final String ACTIVATED = "activated";
        public static final String DEACTIVATED = "deactivated";
    }

    public static class OrgStatus {
        private OrgStatus() {
        }

        public static final String AVAILABLE = "available";
        public static final String DISABLED = "disabled";
        public static final String CREATED = "created";
    }

    public static class BrandApplicationStatus {
        private BrandApplicationStatus() {
        }

        public static final String APPROVED = "approved";
        public static final String REJECTED = "rejected";
        public static final String CREATED = "created";
        public static final String PAID = "paid";
    }

    public static class OrgType {
        private OrgType() {
        }

        public static final String TECH = "tech";
        public static final String CARRIER = "carrier";
        public static final String BRAND = "brand";
    }

    public static class MktDrawPrizeStatus {
        private MktDrawPrizeStatus() {
        }

        public static final String CREATED = "created";
        public static final String SUBMIT = "submit";
        public static final String PAID = "paid";
        public static final String INVALID = "invalid";

    }

    public static class MktType {
        private MktType() {
        }

        public static final String DRAW = "DRAW";
        public static final String REDPACKETS = "REDPACKETS";
        public static final String ENVELOPE = "ENVELOPE";
        public static final String SHAKE = "SHAKE";

    }


    public static class MktStatus {
        private MktStatus() {
        }

        public static final String CREATED = "created";
        public static final String AVAILABLE = "available";
        public static final String PAID = "paid";
        public static final String DISABLED = "disabled";
        public static final String REJECTED = "rejected";
        public static final String DELETED = "deleted";
        public static final String AVALAIBLESTATUS = "availablestatus";
        public static final List<String> AVALAIBLE_STATUS = Arrays.asList("created", "available", "disabled", "paid");
        public static final List<String> ANALYZE_STATUS = Arrays.asList("available", "disabled", "paid");
    }

    public static class PaymentStatus {
        private PaymentStatus() {
        }

        public static final String CREATED = "created";
        public static final String PAID = "paid";
        public static final String FAILED = "failed";
    }

    public static class PaymentType {
        private PaymentType() {
        }

        public static final String ALIPAY = "alipay";
    }


    public static class PermissionRegionType {
        private PermissionRegionType() {
        }

        public static final String DEFAULT = "default";

    }

    public static class TaskFileType {
        private TaskFileType() {
        }

        public static final String PACKAGE = "package";
        public static final String TRACE = "trace";
    }

    public static class TaskFileStatus {
        private TaskFileStatus() {
        }

        public static final String CREATED = "created";
        public static final String UPLOADED = "uploaded";
        public static final String PROCESSING = "processing";
        public static final String FINISHED = "finished";
    }

    public static class MktPrizeCostType{
        private MktPrizeCostType(){}

        public static final String MOBILE_FEE = "mobile fee";
        public static final String MOBILE_FLOW = "mobile flow";
    }

    public enum LookupType {

        ProductStatus("product_status"),

        ProductKeyType("product_key_type"),

        ProductKeyBatchStatus("product_key_batch_status"),

        PermissionResource("permission_resource"),

        PermissionAction("permission_action");

        private String type;

        LookupType(String type) {
            this.type = type;
        }

        public static LookupType toLookupType(String typeCode) {
            if (typeCode == null)
                return null;
            for (LookupType type : LookupType.values()) {
                if (typeCode.equals(type.type))
                    return type;
            }
            return null;
        }

    }

}

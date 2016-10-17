package com.yunsoo.auth;

import java.util.Arrays;
import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/27
 * Descriptions:
 */
public final class Constants {
    private Constants() {
    }

    public static final String SYSTEM_ACCOUNT_ID = "0010000000000000000";

    public static class AccountStatus {
        private AccountStatus() {
        }

        public static final String CREATED = "created";
        public static final String AVAILABLE = "available";
        public static final String DISABLED = "disabled";

        public static final List<String> ALL = Arrays.asList(CREATED, AVAILABLE, DISABLED);
    }

    public static class ApplicationType {
        private ApplicationType() {
        }

        public static final String WEB = "web";
        public static final String PACKAGE = "package";
        public static final String TRACE = "trace";

    }

    public static class OrgType {
        private OrgType() {
        }

        public static final String TECH = "tech";
        public static final String CARRIER = "carrier";
        public static final String BRAND = "brand";

        public static final List<String> ALL = Arrays.asList(TECH, CARRIER, BRAND);
    }

    public static class OrgStatus {
        private OrgStatus() {
        }

        public static final String CREATED = "created";
        public static final String AVAILABLE = "available";
        public static final String DISABLED = "disabled";

        public static final List<String> ALL = Arrays.asList(CREATED, AVAILABLE, DISABLED);
    }

    public static class DeviceStatus {
        private DeviceStatus() {
        }

        public static final String AVAILABLE = "available";
        public static final String DISABLED = "disabled";
        public static final String ONLINE = "online";
        public static final String OFFLINE = "offline";
    }

    public static class PermissionRegionType {
        private PermissionRegionType() {
        }

        public static final String DEFAULT = "default";
        public static final String CUSTOM = "custom";
    }
}

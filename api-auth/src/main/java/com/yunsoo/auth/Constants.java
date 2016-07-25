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

    public static class HttpHeaderName {
        private HttpHeaderName() {
        }

        public static final String ACCESS_TOKEN = "X-YS-AccessToken";
        public static final String APP_ID = "X-YS-AppId";
        public static final String DEVICE_ID = "X-YS-DeviceId";
    }

    public static class AccountStatus {
        private AccountStatus() {
        }

        public static final String CREATED = "created";
        public static final String AVAILABLE = "available";
        public static final String DISABLED = "disabled";

        public static final List<String> ALL = Arrays.asList(CREATED, AVAILABLE, DISABLED);
    }

    public static class OrgStatus {
        private OrgStatus() {
        }

        public static final String CREATED = "created";
        public static final String AVAILABLE = "available";
        public static final String DISABLED = "disabled";

        public static final List<String> ALL = Arrays.asList(CREATED, AVAILABLE, DISABLED);
    }


    public static class PermissionRegionType {
        private PermissionRegionType() {
        }

        public static final String DEFAULT = "default";
    }
}
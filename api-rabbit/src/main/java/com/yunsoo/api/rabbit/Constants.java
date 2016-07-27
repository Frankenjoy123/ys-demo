package com.yunsoo.api.rabbit;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/27
 * Descriptions:
 */
public final class Constants {
    private Constants() {
    }

    public static class Ids {
        public static final String SYSTEM_ACCOUNT_ID = "0010000000000000000";
        public static final String ANONYMOUS_USER_ID = "0020000000000000000";
        public static final String YUNSU_ORG_ID = "2k0r1l55i2rs5544wz5";
    }

    public static class HttpHeaderName {
        public static final String ACCESS_TOKEN = "X-YS-AccessToken";
        public static final String APP_ID = "X-YS-AppId";
        public static final String DEVICE_ID = "X-YS-DeviceId";
        public static final String CONTENT_RANGE = "X-YS-ContentRange";
    }

    public static class CookieName {
        public static final String YSID = "YSID";
    }

    public static class VerificationResult {
        public static final String REAL = "real";
        public static final String UNCERTAIN = "uncertain";
        public static final String FAKE = "fake";
    }

}

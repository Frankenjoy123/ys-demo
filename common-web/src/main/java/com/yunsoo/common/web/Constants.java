package com.yunsoo.common.web;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/27
 * Descriptions:
 */
public final class Constants {
    private Constants() {
    }

    public static class HttpHeaderName {
        private HttpHeaderName() {
        }

        public static final String ACCESS_TOKEN = "X-YS-AccessToken";
        public static final String APP_ID = "X-YS-AppId";
        public static final String DEVICE_ID = "X-YS-DeviceId";
        public static final String CONTENT_RANGE = "X-YS-ContentRange";
    }

}

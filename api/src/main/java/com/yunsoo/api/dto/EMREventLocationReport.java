package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by  : Haitao
 * Created on  : 2016/7/27
 * Descriptions: Scan, Share, Store_url, Comment report
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EMREventLocationReport {

    @JsonProperty("data")
    private NameValue[] provinceData;

    public NameValue[] getProvinceData() {
        return provinceData;
    }

    public void setProvinceData(NameValue[] provinceData) {
        this.provinceData = provinceData;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public final static class NameValue {
        @JsonProperty("name")
        private String name;

        @JsonProperty("scan_event_count")
        private int scanEventCount;

        @JsonProperty("share_event_count")
        private int shareEventCount;

        @JsonProperty("storeUrl_event_count")
        private int storeUrlEventCount;

        @JsonProperty("comment_event_count")
        private int commentEventCount;

        @JsonProperty("scan_user_count")
        private int scanUserCount;

        @JsonProperty("share_user_count")
        private int shareUserCount;

        @JsonProperty("storeUrl_user_count")
        private int storeUrlUserCount;

        @JsonProperty("comment_user_count")
        private int commentUserCount;

        @JsonProperty("city")
        private NameValue[] cityData;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getScanEventCount() {
            return scanEventCount;
        }

        public void setScanEventCount(int scanEventCount) {
            this.scanEventCount = scanEventCount;
        }

        public int getShareEventCount() {
            return shareEventCount;
        }

        public void setShareEventCount(int shareEventCount) {
            this.shareEventCount = shareEventCount;
        }

        public int getStoreUrlEventCount() {
            return storeUrlEventCount;
        }

        public void setStoreUrlEventCount(int storeUrlEventCount) {
            this.storeUrlEventCount = storeUrlEventCount;
        }

        public int getCommentEventCount() {
            return commentEventCount;
        }

        public void setCommentEventCount(int commentEventCount) {
            this.commentEventCount = commentEventCount;
        }

        public int getScanUserCount() {
            return scanUserCount;
        }

        public void setScanUserCount(int scanUserCount) {
            this.scanUserCount = scanUserCount;
        }

        public int getShareUserCount() {
            return shareUserCount;
        }

        public void setShareUserCount(int shareUserCount) {
            this.shareUserCount = shareUserCount;
        }

        public int getStoreUrlUserCount() {
            return storeUrlUserCount;
        }

        public void setStoreUrlUserCount(int storeUrlUserCount) {
            this.storeUrlUserCount = storeUrlUserCount;
        }

        public int getCommentUserCount() {
            return commentUserCount;
        }

        public void setCommentUserCount(int commentUserCount) {
            this.commentUserCount = commentUserCount;
        }

        public NameValue[] getCityData() {
            return cityData;
        }

        public void setCityData(NameValue[] cityData) {
            this.cityData = cityData;
        }
    }

}

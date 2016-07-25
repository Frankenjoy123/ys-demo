package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by  : Haitao
 * Created on  : 2016/7/22
 * Descriptions: Scan, Share, Store_url, Comment report
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EMREventAnalysisReport {

    @JsonProperty("date")
    private String[] date;

    @JsonProperty("event_count")
    private EventCount eventCount;

    @JsonProperty("user_count")
    private UserCount userCount;

    public final static class EventCount {
        @JsonProperty("scan_event_count")
        private int[] scanEventCount;

        @JsonProperty("share_event_count")
        private int[] shareEventCount;

        @JsonProperty("storeUrl_event_count")
        private int[] storeUrlEventCount;

        @JsonProperty("comment_event_count")
        private int[] commentEventCount;

        public int[] getScanEventCount() {
            return scanEventCount;
        }

        public void setScanEventCount(int[] scanEventCount) {
            this.scanEventCount = scanEventCount;
        }

        public int[] getShareEventCount() {
            return shareEventCount;
        }

        public void setShareEventCount(int[] shareEventCount) {
            this.shareEventCount = shareEventCount;
        }

        public int[] getStoreUrlEventCount() {
            return storeUrlEventCount;
        }

        public void setStoreUrlEventCount(int[] storeUrlEventCount) {
            this.storeUrlEventCount = storeUrlEventCount;
        }

        public int[] getCommentEventCount() {
            return commentEventCount;
        }

        public void setCommentEventCount(int[] commentEventCount) {
            this.commentEventCount = commentEventCount;
        }
    }

    public final static class UserCount {
        @JsonProperty("scan_user_count")
        private int[] scanUserCount;

        @JsonProperty("share_user_count")
        private int[] shareUserCount;

        @JsonProperty("storeUrl_user_count")
        private int[] storeUrlUserCount;

        @JsonProperty("comment_user_count")
        private int[] commentUserCount;

        public int[] getScanUserCount() {
            return scanUserCount;
        }

        public void setScanUserCount(int[] scanUserCount) {
            this.scanUserCount = scanUserCount;
        }

        public int[] getShareUserCount() {
            return shareUserCount;
        }

        public void setShareUserCount(int[] shareUserCount) {
            this.shareUserCount = shareUserCount;
        }

        public int[] getStoreUrlUserCount() {
            return storeUrlUserCount;
        }

        public void setStoreUrlUserCount(int[] storeUrlUserCount) {
            this.storeUrlUserCount = storeUrlUserCount;
        }

        public int[] getCommentUserCount() {
            return commentUserCount;
        }

        public void setCommentUserCount(int[] commentUserCount) {
            this.commentUserCount = commentUserCount;
        }
    }

    public String[] getDate() {
        return date;
    }

    public void setDate(String[] date) {
        this.date = date;
    }

    public EventCount getEventCount() {
        return eventCount;
    }

    public void setEventCount(EventCount eventCount) {
        this.eventCount = eventCount;
    }

    public UserCount getUserCount() {
        return userCount;
    }

    public void setUserCount(UserCount userCount) {
        this.userCount = userCount;
    }
}

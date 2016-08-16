package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by  : Haitao
 * Created on  : 2016/7/22
 * Descriptions: Scan, Share, Store_url, Comment Count for event and user
 */
public class EMREventCountObject {

    @JsonProperty("scan_event_count")
    private int scanEventCount;

    @JsonProperty("scan_user_count")
    private int scanUserCount;

    @JsonProperty("share_event_count")
    private int shareEventCount;

    @JsonProperty("share_user_count")
    private int shareUserCount;

    @JsonProperty("storeUrl_event_count")
    private int storeUrlEventCount;

    @JsonProperty("storeUrl_user_count")
    private int storeUrlUserCount;

    @JsonProperty("comment_event_count")
    private int commentEventCount;

    @JsonProperty("comment_user_count")
    private int commentUserCount;

    public int getScanEventCount() {
        return scanEventCount;
    }

    public void setScanEventCount(int scanEventCount) {
        this.scanEventCount = scanEventCount;
    }

    public int getScanUserCount() {
        return scanUserCount;
    }

    public void setScanUserCount(int scanUserCount) {
        this.scanUserCount = scanUserCount;
    }

    public int getShareEventCount() {
        return shareEventCount;
    }

    public void setShareEventCount(int shareEventCount) {
        this.shareEventCount = shareEventCount;
    }

    public int getShareUserCount() {
        return shareUserCount;
    }

    public void setShareUserCount(int shareUserCount) {
        this.shareUserCount = shareUserCount;
    }

    public int getStoreUrlEventCount() {
        return storeUrlEventCount;
    }

    public void setStoreUrlEventCount(int storeUrlEventCount) {
        this.storeUrlEventCount = storeUrlEventCount;
    }

    public int getStoreUrlUserCount() {
        return storeUrlUserCount;
    }

    public void setStoreUrlUserCount(int storeUrlUserCount) {
        this.storeUrlUserCount = storeUrlUserCount;
    }

    public int getCommentEventCount() {
        return commentEventCount;
    }

    public void setCommentEventCount(int commentEventCount) {
        this.commentEventCount = commentEventCount;
    }

    public int getCommentUserCount() {
        return commentUserCount;
    }

    public void setCommentUserCount(int commentUserCount) {
        this.commentUserCount = commentUserCount;
    }

    public EMREventCountObject() {
        this.setScanEventCount(0);
        this.setScanUserCount(0);
        this.setShareEventCount(0);
        this.setShareUserCount(0);
        this.setStoreUrlEventCount(0);
        this.setStoreUrlUserCount(0);
        this.setCommentEventCount(0);
        this.setCommentUserCount(0);
    }
}

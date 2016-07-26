package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.EMRUserProductEventStasticsObject;

import java.io.Serializable;

/**
 * Created by Admin on 6/16/2016.
 */
public class EMRUserProductEventStastics implements Serializable {
    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("scan_count")
    private int scanCount;

    @JsonProperty("draw_count")
    private int drawCount;

    @JsonProperty("win_count")
    private int winCount;

    @JsonProperty("reward_count")
    private int rewardCount;

    @JsonProperty("comment_count")
    private int commentCount;

    @JsonProperty("share_count")
    private int shareCount;

    @JsonProperty("store_count")
    private int storeCount;

    public int getRewardCount() {
        return rewardCount;
    }

    public void setRewardCount(int rewardCount) {
        this.rewardCount = rewardCount;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    public int getDrawCount() {
        return drawCount;
    }

    public void setDrawCount(int drawCount) {
        this.drawCount = drawCount;
    }

    public int getScanCount() {
        return scanCount;
    }

    public void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public int getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(int storeCount) {
        this.storeCount = storeCount;
    }

    public EMRUserProductEventStastics() {
    }

    public EMRUserProductEventStastics(EMRUserProductEventStasticsObject object) {
        if (object != null) {
            this.setOrgId(object.getOrgId());
            this.setDrawCount(object.getDrawCount());
            this.setProductBaseId(object.getProductBaseId());
            this.setProductName(object.getProductName());
            this.setRewardCount(object.getRewardCount());
            this.setScanCount(object.getScanCount());
            this.setWinCount(object.getWinCount());
            this.setStoreCount(object.getStoreCount());
            this.setShareCount(object.getShareCount());
            this.setCommentCount(object.getCommentCount());
        }
    }
}

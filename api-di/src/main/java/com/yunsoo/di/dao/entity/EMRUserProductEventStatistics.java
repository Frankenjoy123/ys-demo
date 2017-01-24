package com.yunsoo.di.dao.entity;

import javax.persistence.Column;

/**
 * Created by yqy09_000 on 6/14/2016.
 */

public class EMRUserProductEventStatistics {

    @Column(name = "product_base_id")
    private String productBaseId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "scan_count")
    private int scanCount;

    @Column(name = "draw_count")
    private int drawCount;

    @Column(name = "win_count")
    private int winCount;

    @Column(name = "reward_count")
    private int rewardCount;

    @Column(name = "comment_count")
    private int commentCount;

    @Column(name = "store_count")
    private int storeCount;

    @Column(name = "share_count")
    private int shareCount;

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public int getScanCount() {
        return scanCount;
    }

    public void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }

    public int getDrawCount() {
        return drawCount;
    }

    public void setDrawCount(int drawCount) {
        this.drawCount = drawCount;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    public int getRewardCount() {
        return rewardCount;
    }

    public void setRewardCount(int rewardCount) {
        this.rewardCount = rewardCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(int storeCount) {
        this.storeCount = storeCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }
}

package com.yunsoo.data.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

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
}

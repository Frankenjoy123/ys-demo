package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by yqy09_000 on 6/15/2016.
 */
public class EMRUserProductEventStasticsObject implements Serializable {

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

    @JsonProperty("reward_count")
    private int rewardCount





            ;
}

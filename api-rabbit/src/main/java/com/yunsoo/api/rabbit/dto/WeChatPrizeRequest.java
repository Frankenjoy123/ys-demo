package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by:   Haitao
 * Created on:   2016-11-22
 * Descriptions:
 */
public class WeChatPrizeRequest {

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("mch_name")
    private String mchName;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("openid")
    private String openId;

    @JsonProperty("wishing")
    private String wishing;

    @JsonProperty("remark")
    private String remark;

    @JsonProperty("action_name")
    private String actionName;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getWishing() {
        return wishing;
    }

    public void setWishing(String wishing) {
        this.wishing = wishing;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}

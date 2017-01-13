package com.yunsoo.third.dto.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by yan on 12/16/2016.
 */
public class WeChatNotifyResult implements Serializable {
    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("order_Type")
    private String orderType;

    @JsonProperty("wechat_order_id")
    private String weChatOrderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getWeChatOrderId() {
        return weChatOrderId;
    }

    public void setWeChatOrderId(String weChatOrderId) {
        this.weChatOrderId = weChatOrderId;
    }
}

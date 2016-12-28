package com.yunsoo.api.third.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 12/16/2016.
 */
public class WeChatPayRequest {

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("nonce_str")
    private String nonceString;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("notify_url")
    private String notifyUrl;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNonceString() {
        return nonceString;
    }

    public void setNonceString(String nonceString) {
        this.nonceString = nonceString;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}

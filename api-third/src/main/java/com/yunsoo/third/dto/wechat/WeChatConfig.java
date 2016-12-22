package com.yunsoo.third.dto.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 11/8/2016.
 */
public class WeChatConfig {

    @JsonProperty("app_id")
    public String appId;

    public Long timestamp;

    public String noncestr;

    public String signature;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}

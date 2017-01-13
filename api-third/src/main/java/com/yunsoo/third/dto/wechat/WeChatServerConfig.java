package com.yunsoo.third.dto.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * Created by yan on 12/27/2016.
 */
public class WeChatServerConfig implements Serializable {

    @JsonProperty("app_id")
    @NotEmpty
    private String appId;

    @JsonProperty("org_id")
    @NotEmpty
    private String orgId;

    @JsonProperty("app_secret")
    @NotEmpty
    private String appSecret;

    @JsonProperty("private_key")
    @NotEmpty
    private String privateKey;

    @JsonProperty("mch_id")
    @NotEmpty
    private String mchId;

    @JsonProperty("ssl_key")
    @NotEmpty
    private byte[] sslKey;

    public byte[] getSslKey() {
        return sslKey;
    }

    public void setSslKey(byte[] sslKey) {
        this.sslKey = sslKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }


}

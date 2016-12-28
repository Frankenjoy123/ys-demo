package com.yunsoo.api.rabbit.third.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by yan on 12/27/2016.
 */
public class WeChatServerConfig {

    @JsonProperty("app_id")
    @NotEmpty
    private String appId;

    @JsonProperty("org_id")
    @NotEmpty
    private String orgId;

    @JsonProperty("app_secret")
    @NotEmpty
    private String appSecret;

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
}

package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/21
 * Descriptions:
 */
public class AccountLoginRequest {

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("identifier")
    private String identifier;

    @JsonProperty("password")
    private String password;


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

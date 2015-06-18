package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by:   Lijian
 * Created on:   2015/4/13
 * Descriptions:
 */
public class AccountPermissionPolicyObject {

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("policy_code")
    private String policyCode;


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

    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }
}

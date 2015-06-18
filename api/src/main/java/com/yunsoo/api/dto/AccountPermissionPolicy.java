package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.AccountPermissionPolicyObject;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/18
 * Descriptions:
 */
public class AccountPermissionPolicy {

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("policy_code")
    private String policyCode;


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

    public AccountPermissionPolicy() {
    }

    public AccountPermissionPolicy(AccountPermissionPolicyObject object) {
        this.orgId = object.getOrgId();
        this.policyCode = object.getPolicyCode();
    }
}

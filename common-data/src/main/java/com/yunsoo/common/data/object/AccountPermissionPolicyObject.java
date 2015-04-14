package com.yunsoo.common.data.object;

/**
 * Created by:   Lijian
 * Created on:   2015/4/13
 * Descriptions:
 */
public class AccountPermissionPolicyObject {

    private Long accountId;
    private String orgId;
    private String policyCode;


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
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

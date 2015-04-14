package com.yunsoo.common.data.object;

/**
 * Created by:   Lijian
 * Created on:   2015/4/13
 * Descriptions:
 */
public class AccountPermissionObject {

    private Long accountId;
    private String orgId;
    private String resourceCode;
    private String actionCode;


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

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
}

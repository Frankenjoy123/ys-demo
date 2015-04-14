package com.yunsoo.api.object;

/**
 * Created by:   Lijian
 * Created on:   2015/4/14
 * Descriptions:
 */
public class TPermission {

    private String orgId;
    private String resourceCode;
    private String actionCode;


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

    @Override
    public String toString() {
        return "permission:" + orgId + ':' + resourceCode + ':' + actionCode;
    }
}

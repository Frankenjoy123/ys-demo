package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.GroupPermissionObject;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/25
 * Descriptions:
 */
public class GroupPermission {

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("resource_code")
    private String resourceCode;

    @JsonProperty("action_code")
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

    public GroupPermission() {
    }

    public GroupPermission(GroupPermissionObject object) {
        this.orgId = object.getOrgId();
        this.resourceCode = object.getResourceCode();
        this.actionCode = object.getActionCode();
    }
}

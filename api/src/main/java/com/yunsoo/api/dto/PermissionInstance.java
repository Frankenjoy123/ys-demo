package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/8
 * Descriptions:
 */
public class PermissionInstance extends Permission {

    @JsonProperty("org_id")
    private String orgId;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public PermissionInstance() {
    }

    public PermissionInstance(String resourceCode, String actionCode, String orgId) {
        super(resourceCode, actionCode);
        this.orgId = orgId;
    }
}

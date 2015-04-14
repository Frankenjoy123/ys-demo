package com.yunsoo.common.data.object;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/14
 * Descriptions:
 */
public class PermissionPolicyObject {

    private String policyCode;
    private String policyName;
    private String description;
    private List<PermissionObject> permissions;

    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PermissionObject> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionObject> permissions) {
        this.permissions = permissions;
    }
}

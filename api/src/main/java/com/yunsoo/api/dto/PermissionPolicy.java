package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.PermissionPolicyObject;

import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/18
 * Descriptions:
 */
public class PermissionPolicy {

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("permissions")
    private List<String> permissions;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }


    public PermissionPolicy() {
    }

    public PermissionPolicy(PermissionPolicyObject policy) {
        if (policy != null) {
            this.code = policy.getCode();
            this.name = policy.getName();
            this.description = policy.getDescription();
            this.permissions = policy.getPermissions();
        }
    }
}

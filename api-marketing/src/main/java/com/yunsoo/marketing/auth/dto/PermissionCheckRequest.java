package com.yunsoo.marketing.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2016-07-27
 * Descriptions:
 */
public class PermissionCheckRequest implements Serializable {

    @JsonProperty("restriction")
    private String restriction;

    @JsonProperty("permission")
    private String permission;

    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public PermissionCheckRequest() {
    }

    public PermissionCheckRequest(String restriction, String permission) {
        this.restriction = restriction;
        this.permission = permission;
    }
}
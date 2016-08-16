package com.yunsoo.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2016-03-31
 * Descriptions:
 */
public class PermissionAllocation implements Serializable {

    @JsonProperty("principal")
    private String principal;

    @JsonProperty("restriction")
    private String restriction;

    @JsonProperty("permission")
    private String permission;

    @JsonProperty("effect")
    private String effect;


    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

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

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

}

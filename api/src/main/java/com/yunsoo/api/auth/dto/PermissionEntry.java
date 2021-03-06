package com.yunsoo.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by:   Lijian
 * Created on:   2016-07-28
 * Descriptions:
 */
public class PermissionEntry {

    @JsonProperty("id")
    private String id;

    @JsonProperty("principal")
    private String principal;

    @JsonProperty("restriction")
    private String restriction;

    @JsonProperty("permission")
    private String permission;

    @JsonProperty("effect")
    private String effect;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

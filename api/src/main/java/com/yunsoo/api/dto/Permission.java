package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/18
 * Descriptions:
 */
public class Permission {

    @JsonProperty("resource_code")
    protected String resourceCode;

    @JsonProperty("action_code")
    protected String actionCode;


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


    public Permission() {
    }

    public Permission(String resourceCode, String actionCode) {
        this.resourceCode = resourceCode;
        this.actionCode = actionCode;
    }

}

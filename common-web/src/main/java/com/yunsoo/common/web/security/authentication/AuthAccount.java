package com.yunsoo.common.web.security.authentication;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2016-10-19
 * Descriptions:
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthAccount implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("details")
    private Map<String, String> details;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
}

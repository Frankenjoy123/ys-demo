package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Zhe on 2015/2/27.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Logistics {
    @JsonProperty("org_id")
    private String OrgId;
    @JsonProperty("org_name")
    private String OrgName;
    //    private String OrgVerified; //removed
    @JsonProperty("message")
    private String message;
    @JsonProperty("location")
    private String location;
    @JsonProperty("datetime")
    private String dateTime;

    public String getOrgId() {
        return OrgId;
    }

    public void setOrgId(String orgId) {
        OrgId = orgId;
    }

    public String getOrgName() {
        return OrgName;
    }

    public void setOrgName(String orgName) {
        OrgName = orgName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}

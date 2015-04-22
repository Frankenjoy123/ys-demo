package com.yunsoo.api.rabbit.dto.basic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.joda.time.DateTime;

/**
 * Created by Zhe on 2015/2/27.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Logistics {
    private String OrgId;
    private String OrgName;
    //    private String OrgVerified; //removed
    private String message;
    private String location;
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

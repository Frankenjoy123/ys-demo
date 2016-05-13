package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.api.dto.detectable.OrgIdDetectable;
import com.yunsoo.common.data.object.DeviceObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

/**
 * Created by  : Zhe
 * Created on  : 2015/5/11
 * Descriptions:
 */
public class Device implements OrgIdDetectable {

    @NotEmpty(message = "id must not be null or empty")
    @JsonProperty("id")
    private String id;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("login_account_id")
    private String loginAccountId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("os")
    private String os;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("check_point_id")
    private String checkPointId;

    @JsonProperty("created_account_id")
    private String createdAccountId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonProperty("modified_account_id")
    private String modifiedAccountId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("modified_datetime")
    private DateTime modifiedDatetime;


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

    public String getLoginAccountId() {
        return loginAccountId;
    }

    public void setLoginAccountId(String loginAccountId) {
        this.loginAccountId = loginAccountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCheckPointId() {
        return checkPointId;
    }

    public void setCheckPointId(String checkPointId) {
        this.checkPointId = checkPointId;
    }

    public String getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(String createdAccountId) {
        this.createdAccountId = createdAccountId;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getModifiedAccountId() {
        return modifiedAccountId;
    }

    public void setModifiedAccountId(String modifiedAccountId) {
        this.modifiedAccountId = modifiedAccountId;
    }

    public DateTime getModifiedDatetime() {
        return modifiedDatetime;
    }

    public void setModifiedDatetime(DateTime modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

    public Device() {
    }

    public Device(DeviceObject obj) {
        if (obj == null) {
            return;
        }
        this.setId(obj.getId());
        this.setOrgId(obj.getOrgId());
        this.setLoginAccountId(obj.getLoginAccountId());
        this.setName(obj.getName());
        this.setOs(obj.getOs());
        this.setCheckPointId(obj.getCheckPointId());
        this.setStatusCode(obj.getStatusCode());
        this.setComments(obj.getComments());
        this.setCreatedAccountId(obj.getCreatedAccountId());
        this.setCreatedDateTime(obj.getCreatedDateTime());
        this.setModifiedAccountId(obj.getModifiedAccountId());
        this.setModifiedDatetime(obj.getModifiedDatetime());
    }

    public DeviceObject toDeviceObject() {
        DeviceObject deviceObject = new DeviceObject();
        deviceObject.setId(this.getId());
        deviceObject.setOrgId(this.getOrgId());
        deviceObject.setLoginAccountId(this.getLoginAccountId());
        deviceObject.setName(this.getName());
        deviceObject.setOs(this.getOs());
        deviceObject.setCheckPointId(this.getCheckPointId());
        deviceObject.setStatusCode(this.getStatusCode());
        deviceObject.setComments(this.getComments());
        deviceObject.setCreatedAccountId(this.getCreatedAccountId());
        deviceObject.setCreatedDateTime(this.getCreatedDateTime());
        deviceObject.setModifiedAccountId(this.getModifiedAccountId());
        deviceObject.setModifiedDatetime(this.getModifiedDatetime());
        return deviceObject;
    }
}

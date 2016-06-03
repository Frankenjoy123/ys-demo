package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2016-05-31
 * Descriptions:
 */
public class TaskFileEntryObject implements Serializable {

    @JsonProperty("file_id")
    private String fileId;

    @NotEmpty(message = "org_id must not be null or empty")
    @JsonProperty("org_id")
    private String orgId;

    @NotEmpty(message = "app_id must not be null or empty")
    @JsonProperty("app_id")
    private String appId;

    @Size(max = 40)
    @JsonProperty("device_id")
    private String deviceId;

    @Size(max = 100)
    @JsonProperty("name")
    private String name;

    @NotEmpty(message = "type_code must not be null or empty")
    @JsonProperty("type_code")
    private String typeCode;

    @NotEmpty(message = "status_code must not be null or empty")
    @JsonProperty("status_code")
    private String statusCode;

    @NotEmpty(message = "created_account_id must not be null or empty")
    @JsonProperty("created_account_id")
    private String createdAccountId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;


    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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
}

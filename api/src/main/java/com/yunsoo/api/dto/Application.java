package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.ApplicationObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

/**
 * Created by:   Lijian
 * Created on:   2015/9/7
 * Descriptions:
 */
public class Application {

    @JsonProperty("id")
    private String id;

    @NotEmpty(message = "name must not be null or empty")
    @JsonProperty("name")
    private String name;

    @NotEmpty(message = "version must not be null or empty")
    @JsonProperty("version")
    private String version;

    @JsonProperty("status_code")
    private String statusCode;

    @NotEmpty(message = "type_code must not be null or empty")
    @JsonProperty("type_code")
    private String typeCode;

    @JsonProperty("description")
    private String description;

    @JsonProperty("system_version")
    private String systemVersion;

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
    private DateTime modifiedDateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
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

    public DateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(DateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public Application() {
    }

    public Application(ApplicationObject object) {
        if (object != null) {
            this.id = object.getId();
            this.name = object.getName();
            this.version = object.getVersion();
            this.statusCode = object.getStatusCode();
            this.typeCode = object.getTypeCode();
            this.description = object.getDescription();
            this.systemVersion = object.getSystemVersion();
            this.createdAccountId = object.getCreatedAccountId();
            this.createdDateTime = object.getCreatedDateTime();
            this.modifiedAccountId = object.getModifiedAccountId();
            this.modifiedDateTime = object.getModifiedDateTime();
        }
    }

    public ApplicationObject toApplicationObject() {
        ApplicationObject object = new ApplicationObject();
        object.setId(this.id);
        object.setName(this.name);
        object.setVersion(this.version);
        object.setStatusCode(this.statusCode);
        object.setTypeCode(this.typeCode);
        object.setDescription(this.description);
        object.setSystemVersion(this.systemVersion);
        object.setCreatedAccountId(this.createdAccountId);
        object.setCreatedDateTime(this.createdDateTime);
        object.setModifiedAccountId(this.modifiedAccountId);
        object.setModifiedDateTime(this.modifiedDateTime);
        return object;
    }
}

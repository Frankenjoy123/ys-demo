package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ApplicationObject;

/**
 * Created by:   Zhe
 * Created on:   2015/6/15
 * Descriptions:
 */
public class Application {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("version")
    private String version;

    @JsonProperty("type_code")
    private String typeCode;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("description")
    private String description;

    @JsonProperty("force_update")
    private boolean forceUpdate;

    @JsonProperty("system_version")
    private String systemVersion;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Application() {
    }

    public Application(ApplicationObject object) {
        if (object != null) {
            this.id = object.getId();
            this.name = object.getName();
            this.version = object.getVersion();
            this.typeCode = object.getTypeCode();
            this.statusCode = object.getStatusCode();
            this.description = object.getDescription();
            this.systemVersion = object.getSystemVersion();
            this.forceUpdate = LookupCodes.ApplicationStatus.FORCE_UPDATE.equals(this.statusCode)? true: false;
        }
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }
}

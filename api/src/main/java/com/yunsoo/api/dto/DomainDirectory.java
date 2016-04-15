package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.DomainDirectoryObject;

/**
 * Created by:   Lijian
 * Created on:   2016-04-14
 * Descriptions:
 */
public class DomainDirectory {

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("org_id")
    private String orgId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public DomainDirectory() {
    }

    public DomainDirectory(DomainDirectoryObject object) {
        if (object != null) {
            this.setName(object.getName());
            this.setDescription(object.getDescription());
            this.setOrgId(object.getOrgId());
        }
    }

    public DomainDirectoryObject toDomainDirectoryObject() {
        DomainDirectoryObject object = new DomainDirectoryObject();
        object.setName(this.getName());
        object.setDescription(this.getDescription());
        object.setOrgId(this.getOrgId());
        return object;
    }
}

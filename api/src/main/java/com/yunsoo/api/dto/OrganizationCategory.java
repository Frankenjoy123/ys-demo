package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.OrganizationCategoryObject;
import org.springframework.beans.BeanUtils;

/**
 * Created by yan on 5/24/2016.
 */
public class OrganizationCategory {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("description")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public OrganizationCategory() {
    }

    public OrganizationCategory(OrganizationCategoryObject object) {
        if (object != null) {
            BeanUtils.copyProperties(object, this);
        }
    }

    public OrganizationCategoryObject toOrganizationCategoryObject() {
        OrganizationCategoryObject object = new OrganizationCategoryObject();
        BeanUtils.copyProperties(this, object);
        return object;

    }
}

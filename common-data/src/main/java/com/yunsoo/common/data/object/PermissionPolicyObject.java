package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/14
 * Descriptions:
 */
public class PermissionPolicyObject implements Serializable {

    @JsonProperty("code")
    private String code;

    @NotEmpty(message = "policy name must not be null or empty")
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @NotEmpty(message = "permissions must not be null or empty")
    @JsonProperty("permissions")
    private List<String> permissions;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}

package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.PermissionPolicyObject;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/18
 * Descriptions:
 */
public class PermissionPolicy {

    @JsonProperty("code")
    private String code;

    @NotNull(message = "policy name must not be null")
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @NotEmpty(message = "permissions must not be null or empty")
    @JsonProperty("permissions")
    private List<Permission> permissions;

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

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }


    public PermissionPolicy() {
    }

    public PermissionPolicy(PermissionPolicyObject permissionPolicyObject) {
        this.code = permissionPolicyObject.getCode();
        this.name = permissionPolicyObject.getName();
        this.description = permissionPolicyObject.getDescription();
        //todo:permissions
//        List<PermissionObject> permissionObjects = permissionPolicyObject.getPermissions();
//        if (permissionObjects != null) {
//            this.permissions = permissionObjects.stream().map(Permission::new).collect(Collectors.toList());
//        }
    }
}

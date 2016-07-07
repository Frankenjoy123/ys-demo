package com.yunsoo.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-03-21
 * Descriptions:
 */
public class PermissionResource implements Serializable {

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("actions")
    private List<PermissionAction> actions;


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

    public List<PermissionAction> getActions() {
        return actions;
    }

    public void setActions(List<PermissionAction> actions) {
        this.actions = actions;
    }

    public PermissionResource() {
    }

//    public PermissionResource(PermissionResourceObject resource, Map<String, PermissionAction> actionMap) {
//        if (resource != null) {
//            this.code = resource.getCode();
//            this.name = resource.getName();
//            this.description = resource.getDescription();
//            if (resource.getActions() != null && actionMap != null) {
//                this.actions = new ArrayList<>();
//                resource.getActions().forEach(a -> {
//                    PermissionAction action = actionMap.get(a);
//                    if (action != null) {
//                        this.actions.add(action);
//                    }
//                });
//            }
//        }
//    }
}

package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/21
 * Descriptions:
 */
public class BrandApplicationLoginRequest {

    @JsonProperty("organization")
    private String organization;

    @JsonProperty("identifier")
    private String identifier;

    @NotNull(message = "password must not be null")
    @JsonProperty("password")
    private String password;


    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

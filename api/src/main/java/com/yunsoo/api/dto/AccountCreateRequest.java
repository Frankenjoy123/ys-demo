package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/19
 * Descriptions:
 */
public class AccountCreateRequest {
    @NotBlank(message="org_id must not be null or empty")
    @JsonProperty("org_id")
    private String orgId;

    @NotBlank(message="identifier must not be null or empty")
    @JsonProperty("identifier")
    private String identifier;

    @NotBlank(message="first_name must not be null or empty")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message="last_name must not be null or empty")
    @JsonProperty("last_name")
    private String lastName;

    @NotBlank(message="email must not be null or empty")
    @JsonProperty("email")
    private String email;

    @NotBlank(message="phone must not be null or empty")
    @JsonProperty("phone")
    private String phone;

    @NotBlank(message="password must not be null or empty")
    @JsonProperty("password")
    private String password;


    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

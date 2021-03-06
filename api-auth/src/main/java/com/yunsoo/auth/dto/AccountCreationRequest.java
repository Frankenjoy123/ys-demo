package com.yunsoo.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.web.security.util.OrgIdDetectable;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/19
 * Descriptions:
 */
public class AccountCreationRequest implements OrgIdDetectable, Serializable {

    @NotBlank(message = "org_id must not be null or empty")
    @JsonProperty("org_id")
    private String orgId;

    @NotBlank(message = "identifier must not be null or empty")
    @JsonProperty("identifier")
    private String identifier;

    @NotNull(message = "first_name must not be null")
    @JsonProperty("first_name")
    private String firstName;

    @NotNull(message = "last_name must not be null")
    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @NotNull(message = "phone must not be null")
    @JsonProperty("phone")
    private String phone;

    @NotBlank(message = "password must not be null or empty")
    @JsonProperty("password")
    private String password;

    @JsonProperty("hash_salt")
    private String hashSalt;

    @JsonProperty("created_account_id")
    private String createdAccountId;


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

    public String getHashSalt() {
        return hashSalt;
    }

    public void setHashSalt(String hashSalt) {
        this.hashSalt = hashSalt;
    }

    public String getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(String createdAccountId) {
        this.createdAccountId = createdAccountId;
    }
}

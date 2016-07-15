package com.yunsoo.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2016-07-11
 * Descriptions:
 */
public class AccountUpdatePasswordRequest implements Serializable {

    @NotBlank(message = "old_password must not be null or empty")
    @JsonProperty("old_password")
    private String oldPassword;

    @NotBlank(message = "new_password must not be null or empty")
    @JsonProperty("new_password")
    private String newPassword;


    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}

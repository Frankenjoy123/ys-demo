package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by:   Lijian
 * Created on:   2015/8/27
 * Descriptions:
 */
public class UserLoginRequest {

    @NotEmpty(message = "phone must not be null or empty")
    @JsonProperty("phone")
    private String phone;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

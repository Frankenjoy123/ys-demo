package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.UserObject;
import org.joda.time.DateTime;

/**
 * Created by:   Lijian
 * Created on:   2015/8/21
 * Descriptions:
 */
public class User {

    @JsonProperty("id")
    private String id;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("name")
    private String name;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("point")
    private Integer point;

    @JsonProperty("address")
    private String address;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }


    public User() {
    }

    public User(UserObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setDeviceId(object.getDeviceId());
            this.setPhone(object.getPhone());
            this.setName(object.getName());
            this.setStatusCode(object.getStatusCode());
            this.setPoint(object.getPoint());
            this.setAddress(object.getAddress());
            this.setCreatedDateTime(object.getCreatedDateTime());
        }
    }

    public UserObject toUserObject() {
        UserObject object = new UserObject();
        object.setId(this.getId());
        object.setDeviceId(this.getDeviceId());
        object.setPhone(this.getPhone());
        object.setName(this.getName());
        object.setStatusCode(this.getStatusCode());
        object.setPoint(this.getPoint());
        object.setAddress(this.getAddress());
        object.setCreatedDateTime(this.getCreatedDateTime());
        return object;
    }
}

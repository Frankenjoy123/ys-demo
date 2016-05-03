package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.UserObject;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by yan on 8/20/2015.
 */
public class User implements Serializable {
    @JsonProperty("id")
    private String id;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("name")
    private String name;

    @NotEmpty(message = "status_code must not be null or empty")
    @JsonProperty("status_code")
    private String statusCode;

    @NotNull(message = "point must not be null")
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
            this.id = object.getId();
            this.statusCode = object.getStatusCode();
            this.phone = object.getPhone();
            this.address = object.getAddress();
            this.deviceId = object.getDeviceId();
            this.createdDateTime = object.getCreatedDateTime();
            this.point = object.getPoint();
        }
    }

}

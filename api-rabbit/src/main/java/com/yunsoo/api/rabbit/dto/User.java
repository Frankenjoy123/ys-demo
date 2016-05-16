package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.hibernate.validator.constraints.NotEmpty;
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

    @JsonProperty("oauth_openid")
    private String oauthOpenid;

    @JsonProperty("email")
    private String email;

    @JsonProperty("age")
    private Integer age;

    @JsonProperty("sex")
    private Boolean sex;

    @JsonProperty("province")
    private String province;

    @JsonProperty("city")
    private String city;

    @JsonProperty("oauth_type_code")
    private String oauthTypeCode;

    @JsonProperty("gravatar_url")
    private String gravatarUrl;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("modified_datetime")
    private DateTime modifiedDateTime;

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

    public String getOauthOpenid() {
        return oauthOpenid;
    }

    public void setOauthOpenid(String oauthOpenid) {
        this.oauthOpenid = oauthOpenid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOauthTypeCode() {
        return oauthTypeCode;
    }

    public void setOauthTypeCode(String oauthTypeCode) {
        this.oauthTypeCode = oauthTypeCode;
    }

    public String getGravatarUrl() {
        return gravatarUrl;
    }

    public void setGravatarUrl(String gravatarUrl) {
        this.gravatarUrl = gravatarUrl;
    }

    public DateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(DateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
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
            this.setOauthOpenid(object.getOauthOpenid());
            this.setAge(object.getAge());
            this.setEmail(object.getEmail());
            this.setSex(object.getSex());
            this.setProvince(object.getProvince());
            this.setCity(object.getCity());
            this.setOauthTypeCode(object.getOauthTypeCode());
            this.setGravatarUrl(object.getGravatarUrl());
            this.setModifiedDateTime(object.getModifiedDateTime());
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
        object.setOauthOpenid(this.getOauthOpenid());
        object.setAge(this.getAge());
        object.setEmail(this.getEmail());
        object.setSex(this.getSex());
        object.setProvince(this.getProvince());
        object.setCity(this.getCity());
        object.setOauthTypeCode(this.getOauthTypeCode());
        object.setGravatarUrl(this.getGravatarUrl());
        object.setModifiedDateTime(this.getModifiedDateTime());
        return object;
    }
}

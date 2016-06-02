package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.EMRUserObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class EMRUser implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("ys_id")
    private String ysId;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("org_name")
    private String orgName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("name")
    private String name;

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

    @JsonProperty("gravatar_url")
    private String gravatarUrl;

    @JsonProperty("address")
    private String address;

    @JsonProperty("wx_openid")
    private String wxOpenid;

    @JsonProperty("user_block_id")
    private String userBlockId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("join_datetime")
    private DateTime joinDateTime;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("device")
    private String device;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getYsId() {
        return ysId;
    }

    public void setYsId(String ysId) {
        this.ysId = ysId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
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

    public String getGravatarUrl() {
        return gravatarUrl;
    }

    public void setGravatarUrl(String gravatarUrl) {
        this.gravatarUrl = gravatarUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWxOpenid() {
        return wxOpenid;
    }

    public void setWxOpenid(String wxOpenid) {
        this.wxOpenid = wxOpenid;
    }

    public DateTime getJoinDateTime() {
        return joinDateTime;
    }

    public void setJoinDateTime(DateTime joinDateTime) {
        this.joinDateTime = joinDateTime;
    }

    public String getUserBlockId() {
        return userBlockId;
    }

    public void setUserBlockId(String userBlockId) {
        this.userBlockId = userBlockId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public EMRUser() {
    }

    public EMRUser(EMRUserObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setUserId(object.getUserId());
            this.setYsId(object.getYsId());
            this.setOrgId(object.getOrgId());
            this.setOrgName(object.getOrgName());
            this.setName(object.getName());
            this.setPhone(object.getPhone());
            this.setAddress(object.getAddress());
            this.setWxOpenid(object.getWxOpenid());
            this.setAge(object.getAge());
            this.setSex(object.getSex());
            this.setEmail(object.getEmail());
            this.setGravatarUrl(object.getGravatarUrl());
            this.setCity(object.getCity());
            this.setProvince(object.getProvince());
            this.setJoinDateTime(object.getJoinDateTime());
            this.setIp(object.getIp());
            this.setDevice(object.getDevice());
        }
    }

}

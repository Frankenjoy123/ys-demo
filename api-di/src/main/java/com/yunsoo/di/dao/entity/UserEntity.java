package com.yunsoo.di.dao.entity;

import org.hibernate.annotations.*;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "di_user")
public class UserEntity implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "ys_id")
    private String ysId;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "phone")
    private String phone;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "age")
    private Integer age;

    @Column(name = "sex")
    private Boolean sex;

    @Column(name = "wx_openid")
    private String wxOpenId;

    @Column(name = "gravatar_url")
    private String gravatarUrl;

    @Column(name="location_id")
    private Integer locationId;

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    @Column(name = "join_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime joinDateTime;

    @ManyToOne(targetEntity = LuProvinceCityEntity.class , fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false,referencedColumnName = "location_id", insertable = false,updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
//    @LazyToOne(LazyToOneOption.NO_PROXY)
    private LuProvinceCityEntity luProvinceCityEntity;

    public LuProvinceCityEntity getLuProvinceCityEntity() {
        return luProvinceCityEntity;
    }

    public void setLuProvinceCityEntity(LuProvinceCityEntity luProvinceCityEntity) {
        this.luProvinceCityEntity = luProvinceCityEntity;
    }

    @OneToMany(targetEntity = UserTagEntity.class, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false),
            @JoinColumn(name = "ys_id", referencedColumnName = "ys_id", insertable = false, updatable = false),
            @JoinColumn(name = "org_id", referencedColumnName = "org_id", insertable = false, updatable = false)
    })
    private Set<UserTagEntity> userTagEntities;

    public Set<UserTagEntity> getUserTagEntities() {
        return userTagEntities;
    }

    public void setUserTagEntities(Set<UserTagEntity> userTagEntities) {
        this.userTagEntities = userTagEntities;
    }

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

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getGravatarUrl() {
        return gravatarUrl;
    }

    public void setGravatarUrl(String gravatarUrl) {
        this.gravatarUrl = gravatarUrl;
    }

    public DateTime getJoinDateTime() {
        return joinDateTime;
    }

    public void setJoinDateTime(DateTime joinDateTime) {
        this.joinDateTime = joinDateTime;
    }

}

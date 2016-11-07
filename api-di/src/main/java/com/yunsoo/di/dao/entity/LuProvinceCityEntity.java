package com.yunsoo.di.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by:   xiaowu
 * Created on:   2016/11/7
 * Descriptions:
 */
@Entity
@Table(name = "lu_province_city")
public class LuProvinceCityEntity {
    @Id
    @Column(name = "location_id")
    private int id;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "tag_id")
    private int tagId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}

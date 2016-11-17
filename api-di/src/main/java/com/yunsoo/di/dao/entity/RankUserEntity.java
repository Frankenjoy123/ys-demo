package com.yunsoo.di.dao.entity;

import com.yunsoo.di.dto.RankUserObject;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Set;


public class RankUserEntity implements Serializable {


    private String userId;

    private String ysId;

    private String orgId;

    private String name;

    private int count;

    private String province;

    private String city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public static RankUserObject toDataObject(RankUserEntity rankUserEntity){
        RankUserObject object=new RankUserObject();
        object.setCount(rankUserEntity.getCount());
        object.setUserId(rankUserEntity.getUserId());
        object.setYsId(rankUserEntity.getYsId());
        object.setOrgId(rankUserEntity.getOrgId());
        object.setName(rankUserEntity.getName());
        object.setProvince(rankUserEntity.getProvince());
        object.setCity(rankUserEntity.getCity());
        return object;
    }
}

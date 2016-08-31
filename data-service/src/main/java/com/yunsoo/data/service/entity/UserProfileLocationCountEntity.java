package com.yunsoo.data.service.entity;

import com.yunsoo.common.data.object.UserProfileLocationCountObject;

import javax.persistence.Column;

/**
 * Created by yqy09_000 on 2016/8/26.
 */
public class UserProfileLocationCountEntity {

    @Column(name = "province")
    private String province;

    @Column(name = "ciy")
    private String city;

    @Column(name = "count")
    private int count;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static UserProfileLocationCountObject toDataObject(UserProfileLocationCountEntity userProfileLocationCountEntity) {
        UserProfileLocationCountObject object = new UserProfileLocationCountObject();
        object.setCount(userProfileLocationCountEntity.getCount());
        object.setCity(userProfileLocationCountEntity.getCity());
        object.setProvince(userProfileLocationCountEntity.getProvince());
        return object;
    }
}

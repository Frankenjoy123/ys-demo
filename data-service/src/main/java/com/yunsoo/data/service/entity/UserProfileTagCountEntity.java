package com.yunsoo.data.service.entity;

import com.yunsoo.common.data.object.UserProfileTagCountObject;

import javax.persistence.Column;

/**
 * Created by yqy09_000 on 2016/8/26.
 */
public class UserProfileTagCountEntity {



    public static class TimeSpan {
        public final static String T00_06 = "00:00-06:00";
        public final static String T06_08 = "06:00-08:00";
        public final static String T08_12 = "08:00-12:00";
        public final static String T12_14 = "12:00-14:00";
        public final static String T14_16 = "14:00-16:00";
        public final static String T16_18 = "16:00-18:00";
        public final static String T18_22 = "18:00-22:00";
        public final static String T22_24 = "22:00-24:00";
    }

    @Column(name = "tag")
    private String tag;

    @Column(name = "count")
    private int count;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static UserProfileTagCountObject toDataObject(UserProfileTagCountEntity userProfileTagCountEntity) {
        UserProfileTagCountObject object = new UserProfileTagCountObject();
        object.setCount(userProfileTagCountEntity.getCount());
        object.setTag(userProfileTagCountEntity.getTag());
        return object;
    }
}

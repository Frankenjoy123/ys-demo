package com.yunsoo.di.dao.entity;

/**
 * Created by yqy09_000 on 2016/11/14.
 */
public class DrawReportEntity {
    // 奖项
    private String drawRuleName;
    private int count;

    private  String id;

    public String getDrawRuleName() {
        return drawRuleName;
    }

    public void setDrawRuleName(String drawRuleName) {
        this.drawRuleName = drawRuleName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package com.yunsoo.di.dao.entity;

import org.joda.time.LocalDate;

/**
 * Created by yqy09_000 on 2016/11/30.
 */
public class PageViewDailyEntity {

    private int pv;
    private int uv;


    private LocalDate date;

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

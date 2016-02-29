package com.yunsoo.common.data.object;

import org.joda.time.LocalDate;

/**
 * Created by Dake Wang on 2016/2/22.
 */
public class KeyUsageReportObject {
    private String date;
    private int keyAmount;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getKeyAmount() {
        return keyAmount;
    }

    public void setKeyAmount(int keyAmount) {
        this.keyAmount = keyAmount;
    }
}

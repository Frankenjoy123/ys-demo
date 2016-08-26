package com.yunsoo.api.rabbit.dto;

/**
 * Created by yan on 8/26/2016.
 */
public class MktDraw {

    private MktDrawRecord record;

    private MktDrawPrize prize;

    public MktDrawRecord getRecord() {
        return record;
    }

    public void setRecord(MktDrawRecord record) {
        this.record = record;
    }

    public MktDrawPrize getPrize() {
        return prize;
    }

    public void setPrize(MktDrawPrize prize) {
        this.prize = prize;
    }
}

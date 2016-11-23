package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by:  haitao
 * Created on:  2016/11/22
 * Descriptions: include draw record and prize info
 */
public class MktDrawInfo {

    @JsonProperty("mkt_draw_record")
    private MktDrawRecord mktDrawRecord;

    @JsonProperty("mkt_draw_prize")
    private MktDrawPrize mktDrawPrize;

    public MktDrawRecord getMktDrawRecord() {
        return mktDrawRecord;
    }

    public void setMktDrawRecord(MktDrawRecord mktDrawRecord) {
        this.mktDrawRecord = mktDrawRecord;
    }

    public MktDrawPrize getMktDrawPrize() {
        return mktDrawPrize;
    }

    public void setMktDrawPrize(MktDrawPrize mktDrawPrize) {
        this.mktDrawPrize = mktDrawPrize;
    }
}

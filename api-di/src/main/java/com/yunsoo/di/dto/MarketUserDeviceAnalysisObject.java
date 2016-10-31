package com.yunsoo.di.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by:   Dake
 * Created on:   2016/5/11
 * Descriptions:
 */
public class MarketUserDeviceAnalysisObject implements Serializable {


    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("draw_date")
    private DateTime drawDate;

    @JsonProperty("device")
    private String device;

    @JsonProperty("count")
    private int count;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("marketing_id")
    private String marketingId;

    public DateTime getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(DateTime drawDate) {
        this.drawDate = drawDate;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(String marketingId) {
        this.marketingId = marketingId;
    }
}

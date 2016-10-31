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
public class MarketUserAreaAnalysisObject implements Serializable {


    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("draw_date")
    private DateTime drawDate;

    @JsonProperty("tag_id")
    private int tagId;

    @JsonProperty("count")
    private int count;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("marketing_id")
    private String marketingId;

    @JsonProperty("tag_name")
    private String tagName;

    public DateTime getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(DateTime drawDate) {
        this.drawDate = drawDate;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
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

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}

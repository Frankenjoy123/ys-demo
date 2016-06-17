package com.yunsoo.common.data.object;

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
public class MarketWinUserLocationAnalysisObject implements Serializable {

    @JsonProperty("count")
    private int count;

    @JsonProperty("province")
    private String province;

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
}

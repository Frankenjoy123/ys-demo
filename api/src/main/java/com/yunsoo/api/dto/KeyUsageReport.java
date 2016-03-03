package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dake Wang on 2016/2/4.
 * <p>
 * <p>
 * {
 * <p>
 * date:[...],
 * data:{pv:[...], uv[...]}
 * <p>
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeyUsageReport {

    @JsonProperty("date")
    private String[] date;

    @JsonProperty("quantity")
    private int[] quantity;

    public String[] getDate() {
        return date;
    }

    public void setDate(String[] date) {
        this.date = date;
    }

    public int[] getQuantity() {
        return quantity;
    }

    public void setQuantity(int[] quantity) {
        this.quantity = quantity;
    }
}






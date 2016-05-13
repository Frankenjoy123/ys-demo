package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dake Wang on 2016/5/12.
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
public class MarketUserCategoryAndValueReport {

    @JsonProperty("data_category")
    private String[] dataCategory;

    @JsonProperty("quantity")
    private int[] quantity;

    public String[] getDataCategory() {
        return dataCategory;
    }

    public void setDataCategory(String[] dataCategory) {
        this.dataCategory = dataCategory;
    }

    public int[] getQuantity() {
        return quantity;
    }

    public void setQuantity(int[] quantity) {
        this.quantity = quantity;
    }
}






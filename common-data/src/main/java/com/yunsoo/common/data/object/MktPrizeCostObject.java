package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by yan on 12/7/2016.
 */
public class MktPrizeCostObject {

    @JsonProperty("draw_record_id")
    private String drawRecordId;

    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private String type;

    @JsonProperty("cost")
    private BigDecimal cost;

    @JsonProperty("order_id")
    private String orderId;

    public String getDrawRecordId() {
        return drawRecordId;
    }

    public void setDrawRecordId(String drawRecordId) {
        this.drawRecordId = drawRecordId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}

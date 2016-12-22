package com.yunsoo.api.rabbit.third.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 12/7/2016.
 */
public class JuheOrder {
    @JsonProperty("cardname")
    private String cardName;

    @JsonProperty("ordercash")
    private String orderPrice;

    @JsonProperty("sporder_id")
    private String juheOrderId;

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getJuheOrderId() {
        return juheOrderId;
    }

    public void setJuheOrderId(String juheOrderId) {
        this.juheOrderId = juheOrderId;
    }
}

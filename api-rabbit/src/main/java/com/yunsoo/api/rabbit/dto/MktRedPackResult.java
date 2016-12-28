package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by yan on 12/23/2016.
 */
public class MktRedPackResult {

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("is_prized")
    private boolean isPrized;

    @JsonProperty("existed")
    private boolean existed;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isPrized() {
        return isPrized;
    }

    public void setIsPrized(boolean isPrized) {
        this.isPrized = isPrized;
    }

    public boolean isExisted() {
        return existed;
    }

    public void setExisted(boolean existed) {
        this.existed = existed;
    }

    public String toString(){
        return "amount: " + amount+", is prized: " + isPrized + ", existed: " + existed;
    }
}

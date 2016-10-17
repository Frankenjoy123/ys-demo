package com.yunsoo.marketing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
public class MarketingRightFlow {
    @JsonProperty("id")
    private String id;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("cost")
    private BigDecimal cost;

    @JsonProperty("cmcc_flow_id")
    private Integer cmccFlowId;

    @JsonProperty("cucc_flow_id")
    private Integer cuccFlowId;

    @JsonProperty("ctcc_flow_id")
    private Integer ctcc_flow_id;

    @JsonProperty("comments")
    private String comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getCmccFlowId() {
        return cmccFlowId;
    }

    public void setCmccFlowId(Integer cmccFlowId) {
        this.cmccFlowId = cmccFlowId;
    }

    public Integer getCuccFlowId() {
        return cuccFlowId;
    }

    public void setCuccFlowId(Integer cuccFlowId) {
        this.cuccFlowId = cuccFlowId;
    }

    public Integer getCtcc_flow_id() {
        return ctcc_flow_id;
    }

    public void setCtcc_flow_id(Integer ctcc_flow_id) {
        this.ctcc_flow_id = ctcc_flow_id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

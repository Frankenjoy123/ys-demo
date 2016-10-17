package com.yunsoo.marketing.dao.entity;

import com.yunsoo.marketing.dao.util.IdGenerator;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.lang.Boolean;
import java.math.BigDecimal;

/**
 * Created by:   Haitao
 * Created on:   2016-10-13
 * Descriptions:
 */
@Entity
@Table(name = "marketing_right_flow")
public class MarketingRightFlowEntity {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = IdGenerator.CLASS)
    @Column(name = "id")
    private String id;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "cmcc_flow_id")
    private Integer cmccFlowId;

    @Column(name = "cucc_flow_id")
    private Integer cuccFlowId;

    @Column(name = "ctcc_flow_id")
    private Integer ctccFlowId;

    @Column(name = "comments")
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

    public Integer getCtccFlowId() {
        return ctccFlowId;
    }

    public void setCtccFlowId(Integer ctccFlowId) {
        this.ctccFlowId = ctccFlowId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

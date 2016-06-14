package com.yunsoo.data.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by yan on 6/13/2016.
 */

@Entity
@Table(name = "mkt_prize_cost")
public class MktPrizeCostEntity {

    @Id
    @Column(name = "draw_record_id")
    private String drawRecordId;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "order_id")
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

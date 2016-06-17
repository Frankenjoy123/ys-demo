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
@Table(name = "mkt_data_flow_list")
public class MktDataFlowEntity {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "type")
    private String type;

    @Column(name = "flow")
    private int dataFlow;

    @Column(name = "price")
    private BigDecimal price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDataFlow() {
        return dataFlow;
    }

    public void setDataFlow(int dataFlow) {
        this.dataFlow = dataFlow;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

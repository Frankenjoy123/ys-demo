package com.yunsoo.data.service.dbmodel;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import java.util.Date;
import javax.persistence.*;

/**
 * Created by Chen Jerry on 3/3/2015.
 */
@Entity
@Table(name = "logistics_check_path")
public class LogisticsCheckPathModel {

    @javax.persistence.Id
    @GeneratedValue
    @Column(name = "id")
    private long Id;

    @Column(name = "product_key")
    private String productKey;

    @Column(name = "action_id")
    private Integer action_id;

    @Column(name = "start_check_point_id")
    private Integer startCheckPoint;

    @Column(name = "start_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime startDate;

    @Column(name = "end_check_point_id", nullable = true)
    private Integer endCheckPoint;

    @Column(name = "end_datetime", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime endDate;

    @Column(name = "operator", nullable = true)
    private Long operator;

    @Column(name = "description", nullable = true)
    private String desc;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        this.Id = id;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public Integer getAction_id() {
        return action_id;
    }

    public void setAction_id(Integer action_id) {
        this.action_id = action_id;
    }

    public Integer getStartCheckPoint() {
        return startCheckPoint;
    }

    public void setStartCheckPoint(Integer startCheckPoint) {
        this.startCheckPoint = startCheckPoint;
    }

    public Integer getEndCheckPoint() {
        return endCheckPoint;
    }

    public void setEndCheckPoint(Integer endCheckPoint) {
        this.endCheckPoint = endCheckPoint;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}

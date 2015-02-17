//package com.yunsoo.dbmodel;
//
//import java.util.Date;
//import javax.persistence.*;
//
///**
// * Qiyong Yu
// */
//@Entity
//@Table(name = "logistics_check_path")
//public class LogisticsCheckPathModel {
//
//    @Id
//    @GeneratedValue
//    @Column(name = "id")
//    private long id;
//
//    @Column(name = "tracking_id")
//    private long tracking_id;
//
//    @Column(name = "status_id")
//    private int status_id;
//
//    @Column(name = "start_check_point_id")
//    private int startCheckPoint;
//
//    @Column(name = "end_check_point_id", nullable = true)
//    private Integer endCheckPoint;
//
//    @Column(name = "start_datetime")
//    private Date startDate;
//
//    @Column(name = "desc", nullable = true)
//    private String desc;
//
//    @Column(name = "end_datetime", nullable = true)
//    private Date endDate;
//
//    @Column(name = "operator", nullable = true)
//    private long operator;
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public long getTracking_id() {
//        return tracking_id;
//    }
//
//    public void setTracking_id(long tracking_id) {
//        this.tracking_id = tracking_id;
//    }
//
//    public int getStatus_id() {
//        return status_id;
//    }
//
//    public void setStatus_id(int status_id) {
//        this.status_id = status_id;
//    }
//
//    public int getStartCheckPoint() {
//        return startCheckPoint;
//    }
//
//    public void setStartCheckPoint(int startCheckPoint) {
//        this.startCheckPoint = startCheckPoint;
//    }
//
//    public int getEndCheckPoint() {
//        return endCheckPoint;
//    }
//
//    public void setEndCheckPoint(int endCheckPoint) {
//        this.endCheckPoint = endCheckPoint;
//    }
//
//    public Date getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(Date startDate) {
//        this.startDate = startDate;
//    }
//
//    public Date getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(Date endDate) {
//        this.endDate = endDate;
//    }
//
//    public long getOperator() {
//        return operator;
//    }
//
//    public void setOperator(long operator) {
//        this.operator = operator;
//    }
//
//    public String getDesc() {
//        return desc;
//    }
//
//    public void setDesc(String desc) {
//        this.desc = desc;
//    }
//
//}

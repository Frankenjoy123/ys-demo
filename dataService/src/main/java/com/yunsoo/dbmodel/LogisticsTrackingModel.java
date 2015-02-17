//package com.yunsoo.dbmodel;
//
//import java.util.Date;
//import java.util.List;
//import javax.persistence.*;
//import org.hibernate.annotations.Type;
//import org.springframework.data.repository.cdi.Eager;
//
///**
// * Qiyong Yu
// */
//@Entity
//@Table(name = "logistics_tracking")
//public class LogisticsTrackingModel {
//
//    @Id
//    @GeneratedValue
//    @Column(name = "id")
//    private long id;
//
//    @Column(name = "org_id")
//    private int org_id;
//
//    @Column(name = "product_id")
//    private int product_id;
//
//    @Column(name = "batch_id", nullable = true)
//    private String batch_id;
//
//    @Column(name = "status_id")
//    private int status_id;
//
//    @Column(name = "created_datetime")
//    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
//    private Date created_time;
//
//    @Column(name = "updated_datetime", nullable = true)
//    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
//    private Date updated_time;
//
//    @OneToMany(fetch = FetchType.EAGER)
//    @JoinColumn(name = "id", referencedColumnName = "tracking_id")
//    private List<LogisticsCheckPathModel> paths;
//
//    public String getBatch_id() {
//        return batch_id;
//    }
//
//    public void setBatch_id(String batch_id) {
//        this.batch_id = batch_id;
//    }
//
//    public Date getCreated_time() {
//        return created_time;
//    }
//
//    public void setCreated_time(Date created_time) {
//        this.created_time = created_time;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public int getOrg_id() {
//        return org_id;
//    }
//
//    public void setOrg_id(int org_id) {
//        this.org_id = org_id;
//    }
//
//    public List<LogisticsCheckPathModel> getPaths() {
//        return paths;
//    }
//
//    public void setPaths(List<LogisticsCheckPathModel> paths) {
//        this.paths = paths;
//    }
//
//    public int getProduct_id() {
//        return product_id;
//    }
//
//    public void setProduct_id(int product_id) {
//        this.product_id = product_id;
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
//    public Date getUpdated_time() {
//        return updated_time;
//    }
//
//    public void setUpdated_time(Date updated_time) {
//        this.updated_time = updated_time;
//    }
//
//}

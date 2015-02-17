//package com.yunsoo.dbmodel;
//
//import org.hibernate.annotations.Type;
//import org.joda.time.DateTime;
//import com.yunsoo.dbmodel.OrganizationModel;
//import javax.persistence.*;
//import java.util.Set;
//
///**
// * Qiyong Yu
// */
//@Entity
//@Table(name = "logistics_key_tracking")
//public class LogisticsKeyTrackingModel {
//    @Id
//    @GeneratedValue
//    @Column(name = "id")
//    private int Id;
//
//    @Column(name = "key")
//    private String key;
//
//    @Column(name = "tracking_id")
//    private long tracking_id;
//
//    @OneToOne
//    @JoinColumn(name = "tracking_id", referencedColumnName = "id")
//    private LogisticsTrackingModel logisticsInfo;
//
//    public String getKey() {
//        return key;
//    }
//
//    public void setKey(String key) {
//        this.key = key;
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
//    public LogisticsTrackingModel getLogisticsInfo() {
//        return logisticsInfo;
//    }
//
//    public void setLogisticsInfo(LogisticsTrackingModel logisticsInfo) {
//        this.logisticsInfo = logisticsInfo;
//    }
//
//
//}

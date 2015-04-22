package com.yunsoo.data.service.dbmodel;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by Zhe on 2015/2/4.
 * Store user's scan records for future analysis.
 */
@Entity
@Table(name = "scan_record")
public class ScanRecordModel {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Basic(optional = false)
    @Column(name = "product_key")
    private String productKey;

    @Column(name = "base_product_id")
    private String baseProductId;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "detail")
    private String detail;

    @Column(name = "created_datetime", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "location")
    private String location;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getBaseProductId() {
        return baseProductId;
    }

    public void setBaseProductId(String baseProductId) {
        this.baseProductId = baseProductId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createDateTime) {
        this.createdDateTime = createDateTime;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

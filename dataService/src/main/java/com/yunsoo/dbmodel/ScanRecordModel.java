package com.yunsoo.dbmodel;

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
    private int baseProductId;

    @Column(name = "client_id")
    private int clientId;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "detail")
    private String detail;

    @Column(name = "created_datetime", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDatetime;

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

    public int getBaseProductId() {
        return baseProductId;
    }

    public void setBaseProductId(int baseProductId) {
        this.baseProductId = baseProductId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public DateTime getCreatedDateTime() {
        return createdDatetime;
    }

    public void setCreatedDateTime(DateTime createDateTime) {
        this.createdDatetime = createDateTime;
    }
}
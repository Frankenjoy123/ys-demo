package com.yunsoo.data.service.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "emr_event")
public class EMREventEntity implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "ys_id")
    private String ysId;

    @Column(name = "name")
    private String name;

    @Column(name = "ip")
    private String ip;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "product_base_id")
    private String productBaseId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_key")
    private String productKey;

    @Column(name = "key_batch_id")
    private String keyBatchId;

    @Column(name = "price_status_code")
    private String priceStatusCode;

    @Column(name = "is_priced")
    private int isPriced;

    @Column(name = "scan_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime scanDateTime;

    @Column(name = "event_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime eventDateTime;

    @Column(name = "wx_openid")
    private String wxOpenId;

    @Column(name = "marketing_id")
    private String marketingId;

    @OneToOne(targetEntity = EMRUserEntity.class, optional = true)
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false),
            @JoinColumn(name = "org_id", referencedColumnName = "org_id", insertable = false, updatable = false)
    })
    private EMRUserEntity userEntity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPriceStatusCode() {
        return priceStatusCode;
    }

    public void setPriceStatusCode(String priceStatusCode) {
        this.priceStatusCode = priceStatusCode;
    }

    public int getIsPriced() {
        return isPriced;
    }

    public void setIsPriced(int isPriced) {
        this.isPriced = isPriced;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public DateTime getScanDateTime() {
        return scanDateTime;
    }

    public void setScanDateTime(DateTime scanDateTime) {
        this.scanDateTime = scanDateTime;
    }

    public DateTime getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(DateTime eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public EMRUserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(EMRUserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getYsId() {
        return ysId;
    }

    public void setYsId(String ysId) {
        this.ysId = ysId;
    }

    public String getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(String marketingId) {
        this.marketingId = marketingId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getKeyBatchId() {
        return keyBatchId;
    }

    public void setKeyBatchId(String keyBatchId) {
        this.keyBatchId = keyBatchId;
    }
}

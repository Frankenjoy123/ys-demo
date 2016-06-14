package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import java.io.Serializable;

public class EMREventObject implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("ys_id")
    private String ysId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("province")
    private String province;

    @JsonProperty("city")
    private String city;

    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_key")
    private String productKey;

    @JsonProperty("key_batch_id")
    private String keyBatchId;

    @JsonProperty("price_status_code")
    private String priceStatusCode;

    @JsonProperty("is_priced")
    private int isPriced;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("scan_datetime")
    private DateTime scanDateTime;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("event_datetime")
    private DateTime eventDateTime;

    @JsonProperty("wx_openid")
    private String wxOpenId;

    @JsonProperty("marketing_id")
    private String marketingId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getYsId() {
        return ysId;
    }

    public void setYsId(String ysId) {
        this.ysId = ysId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
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

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(String marketingId) {
        this.marketingId = marketingId;
    }
}

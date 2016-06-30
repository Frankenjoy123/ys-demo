package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.MktDrawRecordObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

/**
 * Created by:  haitao
 * Created on:  2016/1/25
 * Descriptions:
 */
public class MktDrawRecord {
    @JsonProperty("id")
    private String id;

    @JsonProperty("scan_record_id")
    private String scanRecordId;

    @JsonProperty("marketing_id")
    private String marketingId;

    @JsonProperty("ysid")
    private String ysid;

    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("product_key")
    private String productKey;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("isPrized")
    private Boolean isPrized;

    @JsonProperty("oauth_openid")
    private String oauthOpenid;

    public String getOauthOpenid() {
        return oauthOpenid;
    }

    public void setOauthOpenid(String oauthOpenid) {
        this.oauthOpenid = oauthOpenid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScanRecordId() {
        return scanRecordId;
    }

    public void setScanRecordId(String scanRecordId) {
        this.scanRecordId = scanRecordId;
    }

    public String getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(String marketingId) {
        this.marketingId = marketingId;
    }

    public String getYsid() {
        return ysid;
    }

    public void setYsid(String ysid) {
        this.ysid = ysid;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getIsPrized() {
        return isPrized;
    }

    public void setIsPrized(Boolean isPrized) {
        this.isPrized = isPrized;
    }

    public MktDrawRecord(){}

    public MktDrawRecord(MktDrawRecordObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setScanRecordId(object.getScanRecordId());
            this.setMarketingId(object.getMarketingId());
            this.setYsid(object.getYsid());
            this.setProductBaseId(object.getProductBaseId());
            this.setProductKey(object.getProductKey());
            this.setCreatedDateTime(object.getCreatedDateTime());
            this.setUserId(object.getUserId());
            this.setIsPrized(object.getIsPrized());
            this.setOauthOpenid(object.getOauthOpenid());
        }
    }

    public MktDrawRecordObject toMktDrawRecordObject() {
        MktDrawRecordObject object = new MktDrawRecordObject();
        object.setId(this.getId());
        object.setScanRecordId(this.getScanRecordId());
        object.setMarketingId(this.getMarketingId());
        object.setYsid(this.getYsid());
        object.setProductBaseId(this.getProductBaseId());
        object.setProductKey(this.getProductKey());
        object.setCreatedDateTime(this.getCreatedDateTime());
        object.setUserId(this.getUserId());
        object.setIsPrized(this.getIsPrized());
        object.setOauthOpenid(this.getOauthOpenid());
        return object;
    }

}

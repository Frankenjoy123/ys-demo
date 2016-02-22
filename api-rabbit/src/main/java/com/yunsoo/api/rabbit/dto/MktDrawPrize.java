package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.MktDrawPrizeObject;
import org.joda.time.DateTime;

/**
 * Created by:  haitao
 * Created on:  2016/1/25
 * Descriptions:
 */
public class MktDrawPrize {
    @JsonProperty("draw_record_id")
    private String drawRecordId;

    @JsonProperty("product_key")
    private String productKey;

    @JsonProperty("scan_record_id")
    private String scanRecordId;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("prize_type_code")
    private String prizeTypeCode;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("paid_datetime")
    private DateTime paidDateTime;

    @JsonProperty("account_type")
    private String accountType;

    @JsonProperty("prize_account")
    private String prizeAccount;

    @JsonProperty("prize_account_name")
    private String prizeAccountName;

    @JsonProperty("comments")
    private String comments;

    public String getDrawRecordId() {
        return drawRecordId;
    }

    public void setDrawRecordId(String drawRecordId) {
        this.drawRecordId = drawRecordId;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getScanRecordId() {
        return scanRecordId;
    }

    public void setScanRecordId(String scanRecordId) {
        this.scanRecordId = scanRecordId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPrizeTypeCode() {
        return prizeTypeCode;
    }

    public void setPrizeTypeCode(String prizeTypeCode) {
        this.prizeTypeCode = prizeTypeCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public DateTime getPaidDateTime() {
        return paidDateTime;
    }

    public void setPaidDateTime(DateTime paidDateTime) {
        this.paidDateTime = paidDateTime;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getPrizeAccount() {
        return prizeAccount;
    }

    public void setPrizeAccount(String prizeAccount) {
        this.prizeAccount = prizeAccount;
    }

    public String getPrizeAccountName() {
        return prizeAccountName;
    }

    public void setPrizeAccountName(String prizeAccountName) {
        this.prizeAccountName = prizeAccountName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public MktDrawPrize(){}

    public MktDrawPrize(MktDrawPrizeObject object) {
        if (object != null) {
            this.setDrawRecordId(object.getDrawRecordId());
            this.setProductKey(object.getProductKey());
            this.setScanRecordId(object.getScanRecordId());
            this.setAmount(object.getAmount());
            this.setMobile(object.getMobile());
            this.setPrizeTypeCode(object.getPrizeTypeCode());
            this.setStatusCode(object.getStatusCode());
            this.setCreatedDateTime(object.getCreatedDateTime());
            this.setPaidDateTime(object.getPaidDateTime());
            this.setAccountType(object.getAccountType());
            this.setPrizeAccount(object.getPrizeAccount());
            this.setPrizeAccountName(object.getPrizeAccountName());
            this.setComments(object.getComments());
        }
    }

    public MktDrawPrizeObject toMktDrawPrizeObject() {
        MktDrawPrizeObject object = new MktDrawPrizeObject();
        object.setDrawRecordId(this.getDrawRecordId());
        object.setProductKey(this.getProductKey());
        object.setScanRecordId(this.getScanRecordId());
        object.setAmount(this.getAmount());
        object.setMobile(this.getMobile());
        object.setPrizeTypeCode(this.getPrizeTypeCode());
        object.setStatusCode(this.getStatusCode());
        object.setCreatedDateTime(this.getCreatedDateTime());
        object.setPaidDateTime(this.getPaidDateTime());
        object.setAccountType(this.getAccountType());
        object.setPrizeAccount(this.getPrizeAccount());
        object.setPrizeAccountName(this.getPrizeAccountName());
        object.setComments(this.getComments());
        return object;
    }
}

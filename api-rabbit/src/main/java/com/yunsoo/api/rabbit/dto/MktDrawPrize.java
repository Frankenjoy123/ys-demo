package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.MktDrawPrizeObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
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

    @JsonProperty("marketing_id")
    private String marketingId;

    @JsonProperty("draw_rule_id")
    private String drawRuleId;

    @JsonProperty("amount")
    private Double amount;

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

    @JsonProperty("prize_content")
    private String prizeContent;

    @JsonProperty("prize_contact_id")
    private String prizeContactId;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("mkt_consumer_right")
    private MktConsumerRight mktConsumerRight;

    @JsonProperty("ys_id")
    private String ysid;

    public MktConsumerRight getMktConsumerRight() {
        return mktConsumerRight;
    }

    public void setMktConsumerRight(MktConsumerRight mktConsumerRight) {
        this.mktConsumerRight = mktConsumerRight;
    }

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

    public String getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(String marketingId) {
        this.marketingId = marketingId;
    }

    public String getDrawRuleId() {
        return drawRuleId;
    }

    public void setDrawRuleId(String drawRuleId) {
        this.drawRuleId = drawRuleId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
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

    public String getPrizeContent() {
        return prizeContent;
    }

    public void setPrizeContent(String prizeContent) {
        this.prizeContent = prizeContent;
    }

    public String getPrizeContactId() {
        return prizeContactId;
    }

    public void setPrizeContactId(String prizeContactId) {
        this.prizeContactId = prizeContactId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getYsid() {
        return ysid;
    }

    public void setYsid(String ysid) {
        this.ysid = ysid;
    }

    public MktDrawPrize(){}

    public MktDrawPrize(MktDrawPrizeObject object) {
        if (object != null) {
            this.setDrawRecordId(object.getDrawRecordId());
            this.setProductKey(object.getProductKey());
            this.setScanRecordId(object.getScanRecordId());
            this.setMarketingId(object.getMarketingId());
            this.setDrawRuleId(object.getDrawRuleId());
            this.setAmount(object.getAmount());
            this.setMobile(object.getMobile());
            this.setPrizeTypeCode(object.getPrizeTypeCode());
            this.setStatusCode(object.getStatusCode());
            this.setCreatedDateTime(object.getCreatedDateTime());
            this.setPaidDateTime(object.getPaidDateTime());
            this.setAccountType(object.getAccountType());
            this.setPrizeAccount(object.getPrizeAccount());
            this.setPrizeAccountName(object.getPrizeAccountName());
            this.setPrizeContent(object.getPrizeContent());
            this.setPrizeContactId(object.getPrizeContactId());
            this.setComments(object.getComments());
        }
    }

    public MktDrawPrizeObject toMktDrawPrizeObject() {
        MktDrawPrizeObject object = new MktDrawPrizeObject();
        object.setDrawRecordId(this.getDrawRecordId());
        object.setProductKey(this.getProductKey());
        object.setScanRecordId(this.getScanRecordId());
        object.setMarketingId(this.getMarketingId());
        object.setDrawRuleId(this.getDrawRuleId());
        object.setAmount(this.getAmount());
        object.setMobile(this.getMobile());
        object.setPrizeTypeCode(this.getPrizeTypeCode());
        object.setStatusCode(this.getStatusCode());
        object.setCreatedDateTime(this.getCreatedDateTime());
        object.setPaidDateTime(this.getPaidDateTime());
        object.setAccountType(this.getAccountType());
        object.setPrizeAccount(this.getPrizeAccount());
        object.setPrizeAccountName(this.getPrizeAccountName());
        object.setPrizeContent(this.getPrizeContent());
        object.setPrizeContactId(this.getPrizeContactId());
        object.setComments(this.getComments());
        return object;
    }
}

package com.yunsoo.data.service.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by  : Haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */
@Entity
@Table(name = "mkt_draw_prize")
public class MktDrawPrizeEntity {
    @Id
    @Column(name = "draw_record_id")
    private String drawRecordId;

    @Column(name = "product_key")
    private String productKey;

    @Column(name = "scan_record_id")
    private String scanRecordId;

    @Column(name = "marketing_id")
    private String marketingId;

    @Column(name = "draw_rule_id")
    private String drawRuleId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "prize_type_code")
    private String prizeTypeCode;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    @Column(name = "paid_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime paidDateTime;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "prize_account")
    private String prizeAccount;

    @Column(name = "prize_account_name")
    private String prizeAccountName;

    @Column(name = "prize_content")
    private String prizeContent;

    @Column(name = "prize_contact_id")
    private String prizeContactId;

    @Column(name = "comments")
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

}

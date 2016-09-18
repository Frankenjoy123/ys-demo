package com.yunsoo.data.service.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;

/**
 * Created by  : Haitao
 * Created on  : 2016/9/14
 * Descriptions:
 */
public class MktDrawPrizeReportEntity {

    @Column(name = "product_key")
    private String productKey;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "prize_account")
    private String prizeAccount;

    @Column(name = "prize_account_name")
    private String prizeAccountName;

    @Column(name = "product_base_name")
    private String productBaseName;

    @Column(name = "ip")
    private String ip;

    @Column(name = "city")
    private String city;

    @Column(name = "rule_name")
    private String ruleName;

    @Column(name = "gravatar_url")
    private String gravatarUrl;

    @Column(name = "oauth_openid")
    private String oauthOpenid;


    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
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

    public String getProductBaseName() {
        return productBaseName;
    }

    public void setProductBaseName(String productBaseName) {
        this.productBaseName = productBaseName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getGravatarUrl() {
        return gravatarUrl;
    }

    public void setGravatarUrl(String gravatarUrl) {
        this.gravatarUrl = gravatarUrl;
    }

    public String getOauthOpenid() {
        return oauthOpenid;
    }

    public void setOauthOpenid(String oauthOpenid) {
        this.oauthOpenid = oauthOpenid;
    }
}

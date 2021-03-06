package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.MktDrawPrizeReportObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

/**
 * Created by:  haitao
 * Created on:  2016/9/14
 * Descriptions:
 */
public class MktDrawPrizeReport {

    @JsonProperty("product_key")
    private String productKey;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonProperty("account_type")
    private String accountType;

    @JsonProperty("prize_account")
    private String prizeAccount;

    @JsonProperty("prize_account_name")
    private String prizeAccountName;

    @JsonProperty("product_base_name")
    private String productBaseName;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("city")
    private String city;

    @JsonProperty("rule_name")
    private String ruleName;

    @JsonProperty("gravatar_url")
    private String gravatarUrl;

    @JsonProperty("oauth_openid")
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

    public MktDrawPrizeReport() {
    }

    public MktDrawPrizeReport(MktDrawPrizeReportObject object) {
        if (object != null) {
            this.setProductKey(object.getProductKey());
            this.setAmount(object.getAmount());
            this.setMobile(object.getMobile());
            this.setStatusCode(object.getStatusCode());
            this.setCreatedDateTime(object.getCreatedDateTime());
            this.setAccountType(object.getAccountType());
            this.setPrizeAccount(object.getPrizeAccount());
            this.setPrizeAccountName(object.getPrizeAccountName());
            this.setProductBaseName(object.getProductBaseName());
            this.setIp(object.getIp());
            this.setCity(object.getCity());
            this.setRuleName(object.getRuleName());
            this.setGravatarUrl(object.getGravatarUrl());
            this.setOauthOpenid(object.getOauthOpenid());
        }
    }

    public MktDrawPrizeReportObject toMktDrawPrizeReportObject() {
        MktDrawPrizeReportObject object = new MktDrawPrizeReportObject();
        object.setProductKey(this.getProductKey());
        object.setAmount(this.getAmount());
        object.setMobile(this.getMobile());
        object.setStatusCode(this.getStatusCode());
        object.setCreatedDateTime(this.getCreatedDateTime());
        object.setAccountType(this.getAccountType());
        object.setPrizeAccount(this.getPrizeAccount());
        object.setPrizeAccountName(this.getPrizeAccountName());
        object.setProductBaseName(this.getProductBaseName());
        object.setIp(this.getIp());
        object.setCity(this.getCity());
        object.setRuleName(this.getRuleName());
        object.setGravatarUrl(this.getGravatarUrl());
        object.setOauthOpenid(this.getOauthOpenid());
        return object;
    }

}

package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by yan on 8/26/2016.
 */
public class MktDraw {

    @JsonProperty("scan_record_id")
    private String scanRecordId;

    @JsonProperty("product_base_id")
    @NotEmpty(message = "product_base_id不能为空")
    private String productBaseId;

    @JsonProperty("product_key")
    @NotEmpty(message = "product_key 不能为空")
    private String productKey;

    @JsonProperty("marketing_id")
    private String marketingId;

    @JsonProperty("ysid")
    private String ysId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("oauth_openid")
    private String oauthOpenId;

    @JsonProperty("prize_account_name")
    private String prizeAccountName;

    public String getScanRecordId() {
        return scanRecordId;
    }

    public void setScanRecordId(String scanRecordId) {
        this.scanRecordId = scanRecordId;
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

    public String getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(String marketingId) {
        this.marketingId = marketingId;
    }

    public String getYsId() {
        return ysId;
    }

    public void setYsId(String ysId) {
        this.ysId = ysId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOauthOpenId() {
        return oauthOpenId;
    }

    public void setOauthOpenId(String oauthOpenId) {
        this.oauthOpenId = oauthOpenId;
    }

    public String getPrizeAccountName() {
        return prizeAccountName;
    }

    public void setPrizeAccountName(String prizeAccountName) {
        this.prizeAccountName = prizeAccountName;
    }

}

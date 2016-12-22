package com.yunsoo.third.dto.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by yan on 11/22/2016.
 */
public class WeChatOrderRequest {

    @JsonProperty("order_id")
    @NotEmpty
    private String id;

    @JsonProperty("product_name")
    @NotEmpty
    private String prodName;

    @JsonProperty("price")
    @NotNull
    @Min(value = 1)
    private BigDecimal price;

    @JsonProperty("openid")
    @NotEmpty
    private String openId;

    @JsonProperty("nonce_str")
    @NotEmpty
    private String nonceString;

    @JsonProperty("notify_url")
    @NotEmpty
    private String notifyUrl;

    @JsonProperty("order_type")
    @NotEmpty
    private String orderType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNonceString() {
        return nonceString;
    }

    public void setNonceString(String nonceString) {
        this.nonceString = nonceString;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}

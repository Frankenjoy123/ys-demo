package com.yunsoo.api.dto;

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
}
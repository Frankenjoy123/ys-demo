package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.PaymentObject;
import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * Created by:  haitao
 * Created on:  2015/4/5
 * Descriptions:
 */
public class Payment {
    @JsonProperty("id")
    private String Id;

    @JsonProperty("brand_application_id")
    private String brandApplicationId;

    @JsonProperty("trade_no")
    private String tradeNo;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("type_code")
    private String typeCode;

    @JsonProperty("pay_totals")
    private BigDecimal payTotals;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("paid_datetime")
    private DateTime paidDateTime;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getBrandApplicationId() {
        return brandApplicationId;
    }

    public void setBrandApplicationId(String brandApplicationId) {
        this.brandApplicationId = brandApplicationId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public BigDecimal getPayTotals() {
        return payTotals;
    }

    public void setPayTotals(BigDecimal payTotals) {
        this.payTotals = payTotals;
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

    public Payment() {
    }

    public Payment(PaymentObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setBrandApplicationId(object.getBrandApplicationId());
            this.setTradeNo(object.getTradeNo());
            this.setStatusCode(object.getStatusCode());
            this.setTypeCode(object.getTypeCode());
            this.setPayTotals(object.getPayTotals());
            this.setCreatedDateTime(object.getCreatedDateTime());
            this.setPaidDateTime(object.getPaidDateTime());
        }
    }

    public PaymentObject toPaymentObject() {
        PaymentObject object = new PaymentObject();
        object.setId(this.getId());
        object.setBrandApplicationId(this.getBrandApplicationId());
        object.setTradeNo(this.getTradeNo());
        object.setStatusCode(this.getStatusCode());
        object.setTypeCode(this.getTypeCode());
        object.setPayTotals(this.getPayTotals());
        object.setCreatedDateTime(this.getCreatedDateTime());
        object.setPaidDateTime(this.getPaidDateTime());
        return object;
    }


}

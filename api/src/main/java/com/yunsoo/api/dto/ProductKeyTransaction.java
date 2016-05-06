package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.ProductKeyTransactionObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/5
 * Descriptions:
 */
public class ProductKeyTransaction implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("product_key_batch_id")
    private String productKeyBatchId;

    @JsonProperty("details")
    private List<Detail> details;

    @JsonProperty("created_account_id")
    private String createdAccountId;

    @JsonProperty("created_datetime")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime createdDateTime;

    @JsonProperty("product_key_batch")
    private ProductKeyBatch keyBatch;

    @JsonProperty("product_base")
    private ProductBase productBase;

    public ProductKeyBatch getKeyBatch() {
        return keyBatch;
    }

    public void setKeyBatch(ProductKeyBatch keyBatch) {
        this.keyBatch = keyBatch;
    }

    public ProductBase getProductBase() {
        return productBase;
    }

    public void setProductBase(ProductBase productBase) {
        this.productBase = productBase;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getProductKeyBatchId() {
        return productKeyBatchId;
    }

    public void setProductKeyBatchId(String productKeyBatchId) {
        this.productKeyBatchId = productKeyBatchId;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public String getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(String createdAccountId) {
        this.createdAccountId = createdAccountId;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public static class Detail {

        @JsonProperty("id")
        private String id;

        @JsonProperty("order_id")
        private String orderId;

        @JsonProperty("quantity")
        private Long quantity;

        @JsonProperty("status_code")
        private String statusCode;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public Long getQuantity() {
            return quantity;
        }

        public void setQuantity(Long quantity) {
            this.quantity = quantity;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

        public Detail(){}

        public Detail(ProductKeyTransactionObject.Detail detail){
            this.setId(detail.getId());
            this.setOrderId(detail.getOrderId());
            this.setQuantity(detail.getQuantity());
            this.setStatusCode(detail.getStatusCode());
        }
    }

    public ProductKeyTransaction(){}

    public ProductKeyTransaction(ProductKeyTransactionObject object){
        this.setId(object.getId());
        this.setOrgId(object.getOrgId());
        this.setCreatedAccountId(object.getCreatedAccountId());
        this.setCreatedDateTime(object.getCreatedDateTime());
        this.setProductKeyBatchId(object.getProductKeyBatchId());
        this.setDetails(object.getDetails().stream().map(Detail::new).collect(Collectors.toList()));
    }
}

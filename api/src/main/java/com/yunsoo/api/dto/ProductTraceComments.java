package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.ProductTraceCommentsObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

/**
 * Created by yan on 10/20/2016.
 */
public class ProductTraceComments {

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "product_key")
    private String productKey;

    @JsonProperty(value = "org_id")
    private String orgId;

    @JsonProperty(value = "product_base_id")
    private String productBaseId;

    @JsonProperty(value = "type_code")
    private String typeCode;

    @JsonProperty(value = "status_code")
    private String statusCode;

    @JsonProperty(value = "comments")
    private String comments;

    @JsonProperty(value = "created_account_id")
    private String createdAccountId;

    @JsonProperty(value = "created_datetime")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime createdDateTime;

    @JsonProperty(value = "modified_account_id")
    private String modifiedAccountId;

    @JsonProperty(value = "modified_datetime")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime modifiedCreatedDateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public String getModifiedAccountId() {
        return modifiedAccountId;
    }

    public void setModifiedAccountId(String modifiedAccountId) {
        this.modifiedAccountId = modifiedAccountId;
    }

    public DateTime getModifiedCreatedDateTime() {
        return modifiedCreatedDateTime;
    }

    public void setModifiedCreatedDateTime(DateTime modifiedCreatedDateTime) {
        this.modifiedCreatedDateTime = modifiedCreatedDateTime;
    }

    public ProductTraceComments(){}

    public ProductTraceComments(ProductTraceCommentsObject object){
        BeanUtils.copyProperties(object, this);
    }

    public static ProductTraceCommentsObject toProductTraceCommentsObject(ProductTraceComments comments){
        ProductTraceCommentsObject object =new ProductTraceCommentsObject();
        BeanUtils.copyProperties(comments, object);
        return object;
    }
}

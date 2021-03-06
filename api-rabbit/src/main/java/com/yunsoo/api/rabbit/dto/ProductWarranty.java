package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.ProductWarrantyObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

/**
 * Created by Admin on 2/19/2016.
 */
public class ProductWarranty {

    @JsonProperty("id")
    private String id;

    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("assign_person_id")
    private String assignPersonId;

    @JsonProperty("assign_person_name")
    private String assignPersonName;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("customer_mobile")
    private String customerMobile;

    @JsonProperty("customer_province")
    private String customerProvince;

    @JsonProperty("customer_city")
    private String customerCity;

    @JsonProperty("customer_address")
    private String customerAddress;

    @JsonProperty("customer_description")
    private String customerDescription;

    @JsonProperty("comments")
    private String comments;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonProperty("modified_account_id")
    private String modifiedAccountId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("modified_datetime")
    private DateTime modifiedDateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getAssignPersonId() {
        return assignPersonId;
    }

    public void setAssignPersonId(String assignPersonId) {
        this.assignPersonId = assignPersonId;
    }

    public String getAssignPersonName() {
        return assignPersonName;
    }

    public void setAssignPersonName(String assignPersonName) {
        this.assignPersonName = assignPersonName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerProvince() {
        return customerProvince;
    }

    public void setCustomerProvince(String customerProvince) {
        this.customerProvince = customerProvince;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerDescription() {
        return customerDescription;
    }

    public void setCustomerDescription(String customerDescription) {
        this.customerDescription = customerDescription;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public DateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(DateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public ProductWarranty() {
    }

    public ProductWarranty(ProductWarrantyObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setOrgId(object.getOrgId());
            this.setAssignPersonId(object.getAssignPersonId());
            this.setAssignPersonName(object.getAssignPersonName());
            this.setComments(object.getComments());
            this.setStatusCode(object.getStatusCode());
            this.setProductBaseId(object.getProductBaseId());
            this.setCustomerAddress(object.getCustomerAddress());
            this.setCustomerCity(object.getCustomerCity());
            this.setCustomerDescription(object.getCustomerDescription());
            this.setCustomerMobile(object.getCustomerMobile());
            this.setCustomerProvince(object.getCustomerProvince());
            this.setCustomerName(object.getCustomerName());
            this.setCreatedDateTime(object.getCreatedDateTime());
            this.setModifiedAccountId(object.getModifiedAccountId());
            this.setModifiedDateTime(object.getModifiedDateTime());
        }
    }

    public ProductWarrantyObject toProductWarrantyObject() {
        ProductWarrantyObject object = new ProductWarrantyObject();
        object.setId(this.getId());
        object.setOrgId(this.getOrgId());
        object.setStatusCode(this.getStatusCode());
        object.setAssignPersonId(this.getAssignPersonId());
        object.setAssignPersonName(this.getAssignPersonName());
        object.setComments(this.getComments());
        object.setProductBaseId(this.getProductBaseId());
        object.setCustomerAddress(this.getCustomerAddress());
        object.setCustomerCity(this.getCustomerCity());
        object.setCustomerDescription(this.getCustomerDescription());
        object.setCustomerMobile(this.getCustomerMobile());
        object.setCustomerProvince(this.getCustomerProvince());
        object.setCustomerName(this.getCustomerName());
        object.setCreatedDateTime(this.getCreatedDateTime());
        object.setModifiedAccountId(this.getModifiedAccountId());
        object.setModifiedDateTime(this.getModifiedDateTime());

        return object;
    }
}

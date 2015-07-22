package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.ProductBaseObject;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/3/19
 * Descriptions:
 */
public class ProductBase {

    @JsonProperty("product_base_details")
    private ProductBaseDetails productBaseDetails;

    @JsonProperty("id")
    private String id;

    @JsonProperty("version")
    private Integer version;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("category_id")
    private String categoryId;

    @JsonProperty("category")
    private ProductCategory category;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("barcode")
    private String barcode;

    @JsonProperty("product_key_type_codes")
    private List<String> productKeyTypeCodes;

    @JsonProperty("product_key_types")
    private List<ProductKeyType> productKeyTypes;

    @JsonProperty("shelf_life")
    private Integer shelfLife;

    @JsonProperty("shelf_life_interval")
    private String shelfLifeInterval;

    @JsonProperty("child_product_count")
    private Integer childProductCount;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("created_account_id")
    private String createdAccountId;

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public List<String> getProductKeyTypeCodes() {
        return productKeyTypeCodes;
    }

    public void setProductKeyTypeCodes(List<String> productKeyTypeCodes) {
        this.productKeyTypeCodes = productKeyTypeCodes;
    }

    public List<ProductKeyType> getProductKeyTypes() {
        return productKeyTypes;
    }

    public void setProductKeyTypes(List<ProductKeyType> productKeyTypes) {
        this.productKeyTypes = productKeyTypes;
    }

    public Integer getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Integer shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getShelfLifeInterval() {
        return shelfLifeInterval;
    }

    public void setShelfLifeInterval(String shelfLifeInterval) {
        this.shelfLifeInterval = shelfLifeInterval;
    }

    public Integer getChildProductCount() {
        return childProductCount;
    }

    public void setChildProductCount(Integer childProductCount) {
        this.childProductCount = childProductCount;
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

    public DateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(DateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }


    public ProductBase() {
    }

    public ProductBase(ProductBaseObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setVersion(object.getVersion());
            this.setOrgId(object.getOrgId());
            this.setStatusCode(object.getStatusCode());
            this.setOrgId(object.getOrgId());
            this.setCategoryId(object.getCategoryId());
            this.setName(object.getName());
            this.setDescription(object.getDescription());
            this.setBarcode(object.getBarcode());
            this.setProductKeyTypeCodes(object.getProductKeyTypeCodes());
            this.setShelfLife(object.getShelfLife());
            this.setShelfLifeInterval(object.getShelfLifeInterval());
            this.setChildProductCount(object.getChildProductCount());
            this.setComments(object.getComments());
            this.setCreatedAccountId(object.getCreatedAccountId());
            this.setCreatedDateTime(object.getCreatedDateTime());
            this.setModifiedAccountId(object.getModifiedAccountId());
            this.setModifiedDateTime(object.getModifiedDateTime());
        }
    }

    public ProductBaseObject toProductBaseObject() {
        ProductBaseObject object = new ProductBaseObject();
        object.setId(this.getId());
        object.setVersion(this.getVersion());
        object.setOrgId(this.getOrgId());
        object.setStatusCode(this.getStatusCode());
        object.setOrgId(this.getOrgId());
        object.setCategoryId(this.getCategoryId());
        object.setName(this.getName());
        object.setDescription(this.getDescription());
        object.setBarcode(this.getBarcode());
        object.setProductKeyTypeCodes(this.getProductKeyTypeCodes());
        object.setShelfLife(this.getShelfLife());
        object.setShelfLifeInterval(this.getShelfLifeInterval());
        object.setChildProductCount(this.getChildProductCount());
        object.setComments(this.getComments());
        object.setCreatedAccountId(this.getCreatedAccountId());
        object.setCreatedDateTime(this.getCreatedDateTime());
        object.setModifiedAccountId(this.getModifiedAccountId());
        object.setModifiedDateTime(this.getModifiedDateTime());
        return object;
    }

    public ProductBaseDetails getProductBaseDetails() {
        return productBaseDetails;
    }

    public void setProductBaseDetails(ProductBaseDetails productBaseDetails) {
        this.productBaseDetails = productBaseDetails;
    }
}

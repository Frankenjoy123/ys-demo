package com.yunsoo.api.dto.basic;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.api.dto.ProductKeyType;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Jerry on 6/1/2015.
 */
public class ProductBaseRequest {

    private String id;
    private ProductCategory category;
    private Integer categoryId;
    private String orgId;
    private String barcode;
    private String name;
    private String comment;
    private Integer shelfLife;
    private String shelfLifeInterval;
    private List<String> productKeyTypeCodes;
    private List<ProductKeyType> productKeyTypes;
    private Integer ChildProductCount;
    private String status;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime createdDateTime;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime modifiedDateTime;

    private String thumbnailURL;

    private String proDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Integer getChildProductCount() {
        return ChildProductCount;
    }

    public void setChildProductCount(Integer childProductCount) {
        ChildProductCount = childProductCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDate) {
        this.createdDateTime = createdDate;
    }

    public DateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(DateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public String getThumbnailURL() {
        return this.thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getProDetails() {
        return proDetails;
    }

    public void setProDetails(String proDetails) {
        this.proDetails = proDetails;
    }
}

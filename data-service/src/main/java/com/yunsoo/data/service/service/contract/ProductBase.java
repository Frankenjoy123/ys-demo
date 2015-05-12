package com.yunsoo.data.service.service.contract;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.data.service.dbmodel.ProductBaseModel;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Zhe
 * Created on:   2015/1/26
 * Descriptions:
 */
public class ProductBase {

    private String id;
    private Integer categoryId;
    private String orgId;
    private String barcode;
    private String name;
    //    private String description;
    private String comment;
    private Integer shelfLife;
    private String shelfLifeInterval;
    private List<String> productKeyTypeCodes;
    private Integer childProductCount;
    private String status;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime createdDateTime;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime modifiedDateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer baseProductId) {
        this.categoryId = baseProductId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgId() {
        return orgId;
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

    public Integer getChildProductCount() {
        return childProductCount;
    }

    public void setChildProductCount(Integer childProductCount) {
        this.childProductCount = childProductCount;
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

    public static ProductBase fromModel(ProductBaseModel model) {
        if (model == null) return null;
        ProductBase productBase = new ProductBase();
        productBase.setId(model.getId());
        productBase.setCategoryId(model.getCategoryId());
        productBase.setOrgId(model.getOrgId());
        productBase.setName(model.getName());
        productBase.setBarcode(model.getBarcode());
        productBase.setComment(model.getComment());
        productBase.setShelfLife(model.getShelfLife());
        productBase.setShelfLifeInterval(model.getShelfLifeInterval());
        String ids = model.getProductKeyTypeCodes();
        if (!StringUtils.isEmpty(ids)) {
            productBase.setProductKeyTypeCodes(
                    Arrays.stream(StringUtils.delimitedListToStringArray(ids, ","))
//                            .map(Integer::parseInt)
                            .collect(Collectors.toList()));
        }
        productBase.setChildProductCount(model.getChildProductCount());
        productBase.setStatus(model.getStatus());
        productBase.setCreatedDateTime(model.getCreatedDateTime());
        productBase.setModifiedDateTime(model.getModifiedDateTime());
        return productBase;
    }

    public static ProductBaseModel toModel(ProductBase productBase) {
        if (productBase == null) return null;
        ProductBaseModel model = new ProductBaseModel();
        model.setId(productBase.getId());
        model.setCategoryId(productBase.getCategoryId());
        model.setOrgId(productBase.getOrgId());
        model.setName(productBase.getName());
        model.setBarcode(productBase.getBarcode());
        model.setComment(productBase.getComment());
        model.setShelfLife(productBase.getShelfLife());
        model.setShelfLifeInterval(productBase.getShelfLifeInterval());
        List<String> ids = productBase.getProductKeyTypeCodes();
        if (ids != null) {
            model.setProductKeyTypeCodes(StringUtils.collectionToDelimitedString(ids, ","));
        }
        model.setChildProductCount(productBase.getChildProductCount());
        model.setStatus(productBase.getStatus());
        model.setCreatedDateTime(productBase.getCreatedDateTime());
        model.setModifiedDateTime(productBase.getModifiedDateTime());
        return model;
    }

    public static List<ProductBase> fromModelList(List<ProductBaseModel> modelList) {
        if (modelList == null) return null;
        return modelList.stream().map(ProductBase::fromModel).collect(Collectors.toList());
    }

    public static List<ProductBaseModel> toModelList(List<ProductBase> productBaseList) {
        if (productBaseList == null) return null;
        return productBaseList.stream().map(ProductBase::toModel).collect(Collectors.toList());
    }

}

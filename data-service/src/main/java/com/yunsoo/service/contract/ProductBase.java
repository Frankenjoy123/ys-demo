package com.yunsoo.service.contract;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.dbmodel.ProductBaseModel;
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

    private Long id;
    private Integer categoryId;
    private Long orgId;
    private String barcode;
    private String name;
    private String description;
    private String details;
    private Integer shelfLife;
    private String shelfLifeInterval;
    private List<Integer> productKeyTypeIds;
    private Boolean active;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime createdDateTime;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime modifiedDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer baseProductId) {
        this.categoryId = baseProductId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getOrgId() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public List<Integer> getProductKeyTypeIds() {
        return productKeyTypeIds;
    }

    public void setProductKeyTypeIds(List<Integer> productKeyTypeIds) {
        this.productKeyTypeIds = productKeyTypeIds;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        productBase.setDescription(model.getDescription());
        productBase.setBarcode(model.getBarcode());
        productBase.setDetails(model.getDetails());
        productBase.setShelfLife(model.getShelfLife());
        productBase.setShelfLifeInterval(model.getShelfLifeInterval());
        String ids = model.getProductKeyTypeIds();
        if (!StringUtils.isEmpty(ids)) {
            productBase.setProductKeyTypeIds(
                    Arrays.stream(StringUtils.delimitedListToStringArray(ids, ","))
                            .map(Integer::parseInt)
                            .collect(Collectors.toList()));
        }
        productBase.setActive(model.getActive());
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
        model.setDescription(productBase.getDescription());
        model.setBarcode(productBase.getBarcode());
        model.setDetails(productBase.getDetails());
        model.setShelfLife(productBase.getShelfLife());
        model.setShelfLifeInterval(productBase.getShelfLifeInterval());
        List<Integer> ids = productBase.getProductKeyTypeIds();
        if (ids != null) {
            model.setProductKeyTypeIds(StringUtils.collectionToDelimitedString(ids, ","));
        }
        model.setActive(productBase.getActive());
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

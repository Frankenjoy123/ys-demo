package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by  : Zhe
 * Created on  : 2015/2/27
 * Descriptions:
 */
public class Organization {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type_code")
    private String typeCode;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("description")
    private String description;

    @JsonProperty("details")
    private String details;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonProperty("products")
    private List<ProductBase> productBaseList;

    public List<ProductBase> getProductBaseList() {
        return productBaseList;
    }

    public void setProductBaseList(List<ProductBase> productBaseList) {
        this.productBaseList = productBaseList;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Organization() {
    }

    public Organization(OrganizationObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setName(object.getName());
            this.setStatusCode(object.getStatusCode());
            this.setDescription(object.getDescription());
            this.setTypeCode(object.getTypeCode());
            this.setDetails(object.getDetails());
            this.setCreatedDateTime(object.getCreatedDateTime());
        }
    }

}

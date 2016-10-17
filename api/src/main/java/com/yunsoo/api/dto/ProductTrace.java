package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

/**
 * Created by yan on 10/10/2016.
 */
public class ProductTrace implements Comparable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("product_key")
    private String productKey;

    @JsonProperty("source_type")
    private String sourceType;

    @JsonProperty("source_id")
    private String sourceId;

    @JsonProperty("source_name")
    private String sourceName;

    @JsonProperty("action")
    private String action;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonProperty("product_count")
    private Integer productCount;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public ProductTrace() {
    }

    @Override
    public int compareTo(Object compare) {
        return this.getCreatedDateTime().compareTo(((ProductTrace) compare).getCreatedDateTime());
    }

}

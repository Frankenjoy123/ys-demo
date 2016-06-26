package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2016-06-23
 * Descriptions:
 */
public class ProductPackageObject implements Serializable {

    @JsonProperty("product_key")
    private String productKey;

    @JsonProperty("parent_product_key")
    private String parentProductKey;

    @JsonProperty("child_product_key_set")
    private Set<String> childProductKeySet;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("package_datetime")
    private DateTime packageDateTime;


    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getParentProductKey() {
        return parentProductKey;
    }

    public void setParentProductKey(String parentProductKey) {
        this.parentProductKey = parentProductKey;
    }

    public Set<String> getChildProductKeySet() {
        return childProductKeySet;
    }

    public void setChildProductKeySet(Set<String> childProductKeySet) {
        this.childProductKeySet = childProductKeySet;
    }

    public DateTime getPackageDateTime() {
        return packageDateTime;
    }

    public void setPackageDateTime(DateTime packageDateTime) {
        this.packageDateTime = packageDateTime;
    }
}

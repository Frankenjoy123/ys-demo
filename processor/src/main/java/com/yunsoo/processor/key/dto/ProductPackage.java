package com.yunsoo.processor.key.dto;

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
 * Created on:   2016-08-16
 * Descriptions:
 */
public class ProductPackage implements Serializable {

    @JsonProperty("key")
    private String key;

    @JsonProperty("parent_key")
    private String parentKey;

    @JsonProperty("child_key_set")
    private Set<String> childKeySet;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("package_datetime")
    private DateTime packageDateTime;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public Set<String> getChildKeySet() {
        return childKeySet;
    }

    public void setChildKeySet(Set<String> childKeySet) {
        this.childKeySet = childKeySet;
    }

    public DateTime getPackageDateTime() {
        return packageDateTime;
    }

    public void setPackageDateTime(DateTime packageDateTime) {
        this.packageDateTime = packageDateTime;
    }

}

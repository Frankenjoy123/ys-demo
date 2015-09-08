package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.LookupCodes;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/2/27
 * Descriptions:
 */
public class LookupObject implements Serializable {

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("type_code")
    private String typeCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.code + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LookupObject)) return false;

        LookupObject that = (LookupObject) o;

        if (!code.equals(that.code)) return false;
        return typeCode.equals(that.typeCode);

    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + typeCode.hashCode();
        return result;
    }

}

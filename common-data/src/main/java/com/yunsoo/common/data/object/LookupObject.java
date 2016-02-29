package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2015/2/27
 * Descriptions:
 */
public class LookupObject implements Serializable {

    @JsonProperty("type_code")
    private String typeCode;

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("active")
    private Boolean active;

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }


    @Override
    public String toString() {
        return this.name + " (" + this.typeCode + ", " + this.code + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.typeCode == null || this.code == null || !(obj instanceof LookupObject)) {
            return false;
        }

        LookupObject entity = (LookupObject) obj;

        return this.typeCode.equals(entity.typeCode) && this.code.equals(entity.code);
    }

    @Override
    public int hashCode() {
        return 31 * typeCode.hashCode() + code.hashCode();
    }

}

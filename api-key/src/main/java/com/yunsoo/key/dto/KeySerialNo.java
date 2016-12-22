package com.yunsoo.key.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by:   Lijian
 * Created on:   2016-12-20
 * Descriptions:
 */
public class KeySerialNo {

    @JsonProperty("org_id")
    private String orgId;

    @NotNull(message = "serial_length must not be null")
    @Min(value = 0, message = "serial_length must not be negative")
    @JsonProperty("serial_length")
    private Integer serialLength;

    @NotNull(message = "offset must not be null")
    @Min(value = 0, message = "offset must not be negative")
    @JsonProperty("offset")
    private Integer offset;

    @JsonProperty("prefix")
    private String prefix;

    @JsonProperty("suffix")
    private String suffix;


    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Integer getSerialLength() {
        return serialLength;
    }

    public void setSerialLength(Integer serialLength) {
        this.serialLength = serialLength;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}

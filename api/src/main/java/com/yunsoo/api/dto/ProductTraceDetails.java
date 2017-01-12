package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by yan on 1/6/2017.
 */
public class ProductTraceDetails {

    @JsonProperty("org_name")
    private String orgName;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_key")
    private String productKey;

    @JsonProperty("list")
    private List<ProductTrace> traceList;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public List<ProductTrace> getTraceList() {
        return traceList;
    }

    public void setTraceList(List<ProductTrace> traceList) {
        this.traceList = traceList;
    }
}

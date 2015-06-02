package com.yunsoo.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Package {

    @JsonProperty("key")
    private String key;

    @JsonProperty("product_count")
    private int productCount;

    @JsonProperty("package_count")
    private int packageCount;

    @JsonProperty("sub_packages")
    private List<Package> subPackages;

    public String getKey() {
        return key;
    }

    public int getPackageCount() {
        return packageCount;
    }

    public int getProductCount() {
        return productCount;
    }

    public List<Package> getSubPackages() {
        return subPackages;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setPackageCount(int packageCount) {
        this.packageCount = packageCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public void setSubPackages(List<Package> subPackages) {
        this.subPackages = subPackages;
    }
}

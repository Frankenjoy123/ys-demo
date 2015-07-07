package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Zhe on 2015/6/3.
 */
public class PackageObject {
    @JsonProperty("key")
    private String key;
    @JsonProperty("product_count")
    private int productCount;
    @JsonProperty("package_count")
    private int packageCount;
    @JsonProperty("sub_packages")
    private List<PackageObject> subPackages;
    @JsonProperty("operator")
    private long operator; // Will join to User table to getById the user info

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public int getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(int packageCount) {
        this.packageCount = packageCount;
    }

    public List<PackageObject> getSubPackages() {
        return subPackages;
    }

    public void setSubPackages(List<PackageObject> subPackages) {
        this.subPackages = subPackages;
    }

    public long getOperator() {
        return operator;
    }

    public void setOperator(long operator) {
        this.operator = operator;
    }


}

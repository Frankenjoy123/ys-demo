package com.yunsoo.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Hope on 2015/3/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Package {

    private String key;
    private int productCount;
    private int packageCount;
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

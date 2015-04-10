package com.yunsoo.data.api.dto;

import com.yunsoo.data.service.service.contract.PackageContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hope on 2015/3/7.
 */
public class PackageDto {

    private String key;
    private int productCount;
    private int packageCount;
    private List<PackageDto> subPackages;
    private long operator; // Will join to User table to get the user info

    public PackageDto() {
    }

    public PackageDto(PackageContract contract) {
        this.key = contract.getKey();
        this.productCount = contract.getProductCount();
        this.packageCount = contract.getPackageCount();
        this.operator = contract.getOperator();
        List<PackageContract> subContracts = contract.getSubPackages();

        if (subContracts != null) {
            List<PackageDto> dtos = new ArrayList<>();
            for (PackageContract subContract : subContracts) {
                dtos.add(new PackageDto(subContract));
            }
            this.subPackages = dtos;
        }
    }

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

    public List<PackageDto> getSubPackages() {
        return subPackages;
    }

    public void setSubPackages(List<PackageDto> subPackages) {
        this.subPackages = subPackages;
    }

    public long getOperator() {
        return operator;
    }

    public void setOperator(long operator) {
        this.operator = operator;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunsoo.service.contract;

import com.yunsoo.dbmodel.ProductPackageModel;
import java.util.Date;
import java.util.List;

/**
 *
 * @author qyu
 */
public class ProductPackage {

    private String key;
    private int productCount;
    private int packageCount;
    private int statusId;
    private List<ProductPackage> subPackages;
    private List<Product> products;
    private long operator; // Will join to User table to get the user info 
    private Date created_datetime;

    public ProductPackage() {
    }

    public ProductPackage(ProductPackageModel model) {
        this.key = model.getKey();
        this.productCount = 0;
        this.packageCount = 0;
        this.operator = model.getOperator();
        this.statusId = model.getStatusId();
        this.created_datetime = model.getCreatedDateTime();

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(int packageCount) {
        this.packageCount = packageCount;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public long getOperator() {
        return operator;
    }

    public void setOperator(long operator) {
        this.operator = operator;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<ProductPackage> getSubPackages() {
        return subPackages;
    }

    public void setSubPackages(List<ProductPackage> subPackages) {
        this.subPackages = subPackages;
    }

    public boolean hasPackages() {
        return packageCount > 0;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public Date getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(Date created_datetime) {
        this.created_datetime = created_datetime;
    }

}

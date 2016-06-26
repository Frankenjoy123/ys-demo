package com.yunsoo.data.service.service.contract;

import com.yunsoo.common.data.object.ProductPackageObject;
import com.yunsoo.data.service.dbmodel.dynamodb.ProductPackageModel;
import org.joda.time.DateTime;

import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2016-06-23
 * Descriptions:
 */
public class ProductPackage {

    private String productKey;

    private String parentProductKey;

    private Set<String> childProductKeySet;

    private DateTime packageDateTime;


    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getParentProductKey() {
        return parentProductKey;
    }

    public void setParentProductKey(String parentProductKey) {
        this.parentProductKey = parentProductKey;
    }

    public Set<String> getChildProductKeySet() {
        return childProductKeySet;
    }

    public void setChildProductKeySet(Set<String> childProductKeySet) {
        this.childProductKeySet = childProductKeySet;
    }

    public DateTime getPackageDateTime() {
        return packageDateTime;
    }

    public void setPackageDateTime(DateTime packageDateTime) {
        this.packageDateTime = packageDateTime;
    }

    public ProductPackage() {
    }

    public ProductPackage(ProductPackageModel model) {
        if (model != null) {
            this.setProductKey(model.getProductKey());
            this.setParentProductKey(model.getParentProductKey());
            this.setChildProductKeySet(model.getChildProductKeySet());
            this.setPackageDateTime(model.getPackageDateTime());
        }
    }

    public ProductPackage(ProductPackageObject obj) {
        if (obj != null) {
            this.setProductKey(obj.getProductKey());
            this.setParentProductKey(obj.getParentProductKey());
            this.setChildProductKeySet(obj.getChildProductKeySet());
            this.setPackageDateTime(obj.getPackageDateTime());
        }
    }

    public ProductPackageObject toProductPackageObject() {
        ProductPackageObject obj = new ProductPackageObject();
        obj.setProductKey(this.getProductKey());
        obj.setParentProductKey(this.getParentProductKey());
        obj.setChildProductKeySet(this.getChildProductKeySet());
        obj.setPackageDateTime(this.getPackageDateTime());
        return obj;
    }
}

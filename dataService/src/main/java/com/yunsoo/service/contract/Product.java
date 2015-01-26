package com.yunsoo.service.contract;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.yunsoo.dbmodel.ProductModel;

import java.util.Date;

/**
 * Created by Lijian on 2015/1/16.
 *
 * Update by Zhe 2015/1/26
 * Import Base Product property,
 * Add FromModel ToModel etc as converter methods.
 */
public class Product {

    private int Id;
    private int baseProductId;
    private int productStatusId;
    private Date manufacturingDate;
    private Date createdDateTime;
    private BaseProduct baseProduct;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public int getBaseProductId() {
        return baseProductId;
    }

    public void setBaseProductId(int baseProductId) {
        this.baseProductId = baseProductId;
    }

    public int getProductStatusId() {
        return productStatusId;
    }

    public void setProductStatusId(int productStatusId) {
        this.productStatusId = productStatusId;
    }

    public Date getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(Date manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDate) {
        this.createdDateTime = createdDate;
    }

    public BaseProduct getBaseProduct() {
        return baseProduct;
    }

    public void setBaseProduct(BaseProduct baseProduct) {
        this.baseProduct = baseProduct;
    }

    public static ProductModel ToModel(Product product) {
        if (product == null) return null;
        ProductModel model = new ProductModel();
        model.setId(product.getId());
        model.setBaseProductId(product.getBaseProductId());
        model.setCreatedDateTime(product.getCreatedDateTime());
        model.setManufacturingDate(product.getManufacturingDate());
        model.setProductStatusId(product.getProductStatusId());
        model.setBaseProductModel(BaseProduct.ToModel(product.getBaseProduct()));
        return model;
    }

    public static Product FromModel(ProductModel model) {
        if (model == null) return null;
        Product product = new Product();
        product.setId(model.getId());
        product.setProductStatusId(model.getProductStatusId());
        product.setManufacturingDate(model.getManufacturingDate());
        product.setCreatedDateTime(model.getCreatedDateTime());
        product.setBaseProductId(model.getBaseProductId());
        product.setBaseProduct(BaseProduct.FromModel(model.getBaseProductModel()));
        return product;
    }
}

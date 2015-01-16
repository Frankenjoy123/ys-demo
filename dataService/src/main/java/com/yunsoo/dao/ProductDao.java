package com.yunsoo.dao;

import java.util.List;

import com.yunsoo.dbmodel.ProductModel;

public interface ProductDao {
    public ProductModel getById(int id);

    public void save(ProductModel productModel);

    public void update(ProductModel productModel);

    public void delete(ProductModel productModel);

    public List<ProductModel> getAllProducts();

}

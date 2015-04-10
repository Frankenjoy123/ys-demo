package com.yunsoo.data.service.dao;

import java.util.List;

import com.yunsoo.data.service.dbmodel.ProductSubCategoryModel;

public interface ProductSubCategoryDao {
    public ProductSubCategoryModel getById(int id);

    public void save(ProductSubCategoryModel productSubCategoryModel);

    public void update(ProductSubCategoryModel productSubCategoryModel);

    public void delete(ProductSubCategoryModel productSubCategoryModel);

    public List<ProductSubCategoryModel> getAllProductSubCategories();
}

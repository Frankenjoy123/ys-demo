package com.yunsoo.dao;

import java.util.List;

import com.yunsoo.dbmodel.ProductCategoryModel;

public interface ProductCategoryDao {
    public ProductCategoryModel getById(int id);

    public List<ProductCategoryModel> getProductCategoriesByParentId(int parentId); //get product-sub-categories by parent category id

    public List<ProductCategoryModel> getRootProductCategories();

    public void save(ProductCategoryModel productCategoryModelModel);

    public void update(ProductCategoryModel productCategoryModelModel);

    public void delete(ProductCategoryModel productCategoryModelModel);

    public List<ProductCategoryModel> getAllProductCategories();
}

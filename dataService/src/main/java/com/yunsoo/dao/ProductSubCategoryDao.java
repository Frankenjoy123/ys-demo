package com.yunsoo.dao;

import java.util.List;

import com.yunsoo.dbmodel.ProductSubCategory;

public interface ProductSubCategoryDao {
	public ProductSubCategory getById(int id);
    public void save(ProductSubCategory productSubCategory);
    public void update(ProductSubCategory productSubCategory);
    public void delete(ProductSubCategory productSubCategory);
    public List<ProductSubCategory> getAllProductSubCategories();
}

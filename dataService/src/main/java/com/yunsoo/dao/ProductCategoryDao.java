package com.yunsoo.dao;

import java.util.List;
import com.yunsoo.model.ProductCategory;

public interface ProductCategoryDao {
	public ProductCategory getById(int id);
    public void save(ProductCategory productCategory);
    public void update(ProductCategory productCategory);
    public void delete(ProductCategory productCategory);
    public List<ProductCategory> getAllProductCategories();
}

package com.yunsoo.service;

import java.util.List;
import com.yunsoo.model.ProductCategory;

public interface ProductCategoryService {

	public ProductCategory getById(int id);

	public void save(ProductCategory productkey);

	public void update(ProductCategory productkey);

	public void delete(ProductCategory productkey);

	public List<ProductCategory> getAllProductCategories();

}

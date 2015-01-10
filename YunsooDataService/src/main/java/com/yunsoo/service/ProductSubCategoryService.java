package com.yunsoo.service;

import java.util.List;
import com.yunsoo.model.ProductSubCategory;

public interface ProductSubCategoryService {
	
	public ProductSubCategory getById(int id);

	public void save(ProductSubCategory productSubCategory);

	public void update(ProductSubCategory productSubCategory);

	public void delete(ProductSubCategory productSubCategory);

	public List<ProductSubCategory> getAllProductSubCategories();

}

package com.yunsoo.data.service.service;

import java.util.List;

import com.yunsoo.data.service.service.contract.ProductCategory;

public interface ProductCategoryService {

	public ProductCategory getById(int id);

	public void save(ProductCategory productCategory);

	public void update(ProductCategory productCategory);

	public void delete(ProductCategory productCategory);

	public List<ProductCategory> getAllProductCategories();

	public List<ProductCategory> getProductCategoriesByParentId(int parentId); //getById product-sub-categories by parent category id

	public List<ProductCategory> getRootProductCategories();

}

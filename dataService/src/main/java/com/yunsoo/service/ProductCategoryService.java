package com.yunsoo.service;

import java.util.List;

import com.yunsoo.dbmodel.ProductCategoryModel;

public interface ProductCategoryService {

	public ProductCategoryModel getById(int id);

	public void save(ProductCategoryModel productCategoryModel);

	public void update(ProductCategoryModel productCategoryModel);

	public void delete(ProductCategoryModel productCategoryModel);

	public List<ProductCategoryModel> getAllProductCategories();

	public List<ProductCategoryModel> getProductCategoriesByParentId(int parentId); //get product-sub-categories by parent category id

	public List<ProductCategoryModel> getRootProductCategories();

}

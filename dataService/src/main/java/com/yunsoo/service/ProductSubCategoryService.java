package com.yunsoo.service;

import java.util.List;

import com.yunsoo.dbmodel.ProductSubCategoryModel;

public interface ProductSubCategoryService {

	public ProductSubCategoryModel getById(int id);

	public void save(ProductSubCategoryModel productSubCategoryModel);

	public void update(ProductSubCategoryModel productSubCategoryModel);

	public void delete(ProductSubCategoryModel productSubCategoryModel);

	public List<ProductSubCategoryModel> getAllProductSubCategories();

}

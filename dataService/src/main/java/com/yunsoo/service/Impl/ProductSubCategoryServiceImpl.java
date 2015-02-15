package com.yunsoo.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunsoo.dao.ProductSubCategoryDao;
import com.yunsoo.dbmodel.ProductSubCategoryModel;
import com.yunsoo.service.ProductSubCategoryService;


@Service("productSubCategoryService")
public class ProductSubCategoryServiceImpl implements ProductSubCategoryService{

	@Autowired
	private ProductSubCategoryDao productSubCategoryDao;
	
	
	@Override
	public ProductSubCategoryModel getById(int id) {
		return productSubCategoryDao.getById(id);
	}

	@Override
	public void save(ProductSubCategoryModel productSubCategoryModel) {
		productSubCategoryDao.save(productSubCategoryModel);
	}

	@Override
	public void update(ProductSubCategoryModel productSubCategoryModel) {
		productSubCategoryDao.update(productSubCategoryModel);
	}

	@Override
	public void delete(ProductSubCategoryModel productSubCategoryModel) {
		productSubCategoryDao.delete(productSubCategoryModel);
	}

	@Override
	@Transactional
	public List<ProductSubCategoryModel> getAllProductSubCategories() {
		return productSubCategoryDao.getAllProductSubCategories();
	}

}

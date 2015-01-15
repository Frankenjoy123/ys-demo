package com.yunsoo.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunsoo.dao.ProductSubCategoryDao;
import com.yunsoo.dbmodel.ProductSubCategory;
import com.yunsoo.service.ProductSubCategoryService;


@Service("productSubCategoryService")
public class ProductSubCategoryServiceImpl implements ProductSubCategoryService{

	@Autowired
	private ProductSubCategoryDao productSubCategoryDao;
	
	
	@Override
	public ProductSubCategory getById(int id) {
		return productSubCategoryDao.getById(id);
	}

	@Override
	public void save(ProductSubCategory productSubCategory) {
		productSubCategoryDao.save(productSubCategory);		
	}

	@Override
	public void update(ProductSubCategory productSubCategory) {
		productSubCategoryDao.update(productSubCategory);		
	}

	@Override
	public void delete(ProductSubCategory productSubCategory) {
		productSubCategoryDao.delete(productSubCategory);	
	}

	@Override
	@Transactional
	public List<ProductSubCategory> getAllProductSubCategories() {
		return productSubCategoryDao.getAllProductSubCategories();
	}

}

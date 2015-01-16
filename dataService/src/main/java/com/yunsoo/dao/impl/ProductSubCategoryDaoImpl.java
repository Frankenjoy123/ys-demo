package com.yunsoo.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.yunsoo.dao.ProductSubCategoryDao;
import com.yunsoo.dbmodel.ProductSubCategoryModel;


@Repository("productSubCategoryDao")
@Transactional
public class ProductSubCategoryDaoImpl implements ProductSubCategoryDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public ProductSubCategoryModel getById(int id) {
		return (ProductSubCategoryModel) sessionFactory.getCurrentSession().get(ProductSubCategoryModel.class,
				id);
	}

	@Override
	public void save(ProductSubCategoryModel productSubCategoryModel) {
		sessionFactory.getCurrentSession().save(productSubCategoryModel);
	}

	@Override
	public void update(ProductSubCategoryModel productSubCategoryModel) {
		sessionFactory.getCurrentSession().update(productSubCategoryModel);
	}

	@Override
	public void delete(ProductSubCategoryModel productSubCategoryModel) {
		sessionFactory.getCurrentSession().delete(productSubCategoryModel);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductSubCategoryModel> getAllProductSubCategories() {
		return sessionFactory.getCurrentSession().createCriteria(ProductSubCategoryModel.class)
				.list();
	}

}

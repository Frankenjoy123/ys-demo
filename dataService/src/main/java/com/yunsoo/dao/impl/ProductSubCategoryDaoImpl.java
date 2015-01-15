package com.yunsoo.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.yunsoo.dao.ProductSubCategoryDao;
import com.yunsoo.dbmodel.ProductSubCategory;


@Repository("productSubCategoryDao")
@Transactional
public class ProductSubCategoryDaoImpl implements ProductSubCategoryDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public ProductSubCategory getById(int id) {
		return (ProductSubCategory) sessionFactory.getCurrentSession().get(ProductSubCategory.class,
				id);
	}

	@Override
	public void save(ProductSubCategory productSubCategory) {
		sessionFactory.getCurrentSession().save(productSubCategory);
	}

	@Override
	public void update(ProductSubCategory productSubCategory) {
		sessionFactory.getCurrentSession().update(productSubCategory);
	}

	@Override
	public void delete(ProductSubCategory productSubCategory) {
		sessionFactory.getCurrentSession().delete(productSubCategory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductSubCategory> getAllProductSubCategories() {
		return sessionFactory.getCurrentSession().createCriteria(ProductSubCategory.class)
				.list();
	}

}

package com.yunsoo.dao.impl;

import java.util.List;

import com.yunsoo.dbmodel.ProductModel;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.yunsoo.dao.ProductDao;

@Repository("productDao")
@Transactional
public class ProductDaoImpl implements ProductDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public ProductModel getById(int id) {
		//to-do, merge with BaseProductModel
		return (ProductModel) sessionFactory.getCurrentSession().get(ProductModel.class,
				id);
	}

	@Override
	public void save(ProductModel productModel) {
		sessionFactory.getCurrentSession().save(productModel);
	}

	@Override
	public void update(ProductModel productModel) {
		sessionFactory.getCurrentSession().update(productModel);
	}

	@Override
	public void delete(ProductModel productModel) {
		sessionFactory.getCurrentSession().delete(productModel);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductModel> getAllProducts() {
		return sessionFactory.getCurrentSession().createCriteria(ProductModel.class)
				.list();
	}

}

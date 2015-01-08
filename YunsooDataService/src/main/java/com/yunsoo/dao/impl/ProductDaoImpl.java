package com.yunsoo.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.yunsoo.dao.ProductDao;
import com.yunsoo.model.Product;

@Repository("productDao")
@Transactional
public class ProductDaoImpl implements ProductDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Product getById(int id) {
		//to-do, merge with BaseProduct
		return (Product) sessionFactory.getCurrentSession().get(Product.class,
				id);
	}

	@Override
	public void save(Product product) {
		sessionFactory.getCurrentSession().save(product);
	}

	@Override
	public void update(Product product) {
		sessionFactory.getCurrentSession().update(product);
	}

	@Override
	public void delete(Product product) {
		sessionFactory.getCurrentSession().delete(product);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getAllProducts() {
		return sessionFactory.getCurrentSession().createCriteria(Product.class)
				.list();
	}

}

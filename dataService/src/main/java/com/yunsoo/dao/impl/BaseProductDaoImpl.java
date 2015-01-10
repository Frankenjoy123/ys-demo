package com.yunsoo.dao.impl;

import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.yunsoo.dao.BaseProductDao;
import com.yunsoo.model.BaseProduct;

@Repository("baseProductDao")
@Transactional
public class BaseProductDaoImpl implements BaseProductDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public BaseProduct getById(int id) {
		return (BaseProduct) sessionFactory.getCurrentSession().get(
				BaseProduct.class, id);
	}

	@Override
	public void save(BaseProduct baseProduct) {
		sessionFactory.getCurrentSession().save(baseProduct);
	}

	@Override
	public void update(BaseProduct baseProduct) {
		sessionFactory.getCurrentSession().update(baseProduct);
	}

	@Override
	public void delete(BaseProduct baseProduct) {
		sessionFactory.getCurrentSession().delete(baseProduct);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BaseProduct> getAllBaseProducts() {
		return sessionFactory.getCurrentSession()
				.createCriteria(BaseProduct.class).list();
	}

}

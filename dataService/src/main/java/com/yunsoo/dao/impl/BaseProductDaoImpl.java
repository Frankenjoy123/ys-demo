package com.yunsoo.dao.impl;

import java.util.List;

import com.yunsoo.dbmodel.BaseProductModel;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.yunsoo.dao.BaseProductDao;

@Repository("baseProductDao")
@Transactional
public class BaseProductDaoImpl implements BaseProductDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public BaseProductModel getById(int id) {
		return (BaseProductModel) sessionFactory.getCurrentSession().get(
				BaseProductModel.class, id);
	}

	@Override
	public void save(BaseProductModel baseProductModel) {
		sessionFactory.getCurrentSession().save(baseProductModel);
	}

	@Override
	public void update(BaseProductModel baseProductModel) {
		sessionFactory.getCurrentSession().update(baseProductModel);
	}

	@Override
	public void delete(BaseProductModel baseProductModel) {
		sessionFactory.getCurrentSession().delete(baseProductModel);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BaseProductModel> getAllBaseProducts() {
		return sessionFactory.getCurrentSession()
				.createCriteria(BaseProductModel.class).list();
	}

}

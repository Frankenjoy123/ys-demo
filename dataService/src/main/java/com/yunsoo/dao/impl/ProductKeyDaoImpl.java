package com.yunsoo.dao.impl;

import java.util.List;
//import org.springframework.orm.hibernate4.HibernateTemplate;
import com.yunsoo.dao.ProductKeyDao;
import com.yunsoo.dbmodel.ProductKey;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("productkeyDao")
@Transactional
public class ProductKeyDaoImpl implements ProductKeyDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public ProductKey getById(int id) {
		return (ProductKey) sessionFactory.getCurrentSession().get(
				ProductKey.class, id);
	}

	@Override
	public void save(ProductKey productKey) {
		sessionFactory.getCurrentSession().save(productKey);
	}

	@Override
	public void update(ProductKey productKey) {
		sessionFactory.getCurrentSession().update(productKey);
	}

	@Override
	public void delete(ProductKey productKey) {
		sessionFactory.getCurrentSession().delete(productKey);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductKey> getAllProductKeys() {
		return sessionFactory.getCurrentSession()
				.createCriteria(ProductKey.class).list();
	}


	// HibernateTemplate template;
	// public void setTemplate(HibernateTemplate template) {
	// this.template = template;
	// }

	// method to return one ProductKey of given id
	// public ProductKey getById(int id) {
	// ProductKey ptKey = (ProductKey) template.get(ProductKey.class, id);
	// return ptKey;
	// }
	//
	// // method to save ProductKey
	// public void saveProductKey(ProductKey e) {
	// template.save(e);
	// }
	//
	// // method to update ProductKey
	// public void updateProductKey(ProductKey e) {
	// template.update(e);
	// }
	//
	// // method to delete ProductKey
	// public void deleteProductKey(ProductKey e) {
	// template.delete(e);
	// }
	//
	// // method to return all ProductKeys
	// public List<ProductKey> getAllProductKey() {
	// List<ProductKey> list = new ArrayList<ProductKey>();
	// list = template.loadAll(ProductKey.class);
	// return list;
	// }

	// public static DaoStatus CreateProductKey(ProductKey prodKey) {
	// try {
	// if (!prodKey.validate()) {
	// return DaoStatus.invalidForPersistence;
	// }
	//
	// Session session = HibernateSessionManager.getSessionFactory()
	// .openSession();
	// session.beginTransaction();
	// session.save(prodKey);
	// session.getTransaction().commit();
	// return DaoStatus.success;
	//
	// } catch (Exception ex) {
	// //to-do: log exception
	// return DaoStatus.fail;
	// }
	// }
}

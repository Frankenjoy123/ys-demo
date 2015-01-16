package com.yunsoo.dao.impl;

import java.util.List;
//import org.springframework.orm.hibernate4.HibernateTemplate;
import com.yunsoo.dao.ProductKeyDao;
import com.yunsoo.dbmodel.ProductKeyModel;
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
	public ProductKeyModel getById(int id) {
		return (ProductKeyModel) sessionFactory.getCurrentSession().get(
				ProductKeyModel.class, id);
	}

	@Override
	public void save(ProductKeyModel productKeyModel) {
		sessionFactory.getCurrentSession().save(productKeyModel);
	}

	@Override
	public void update(ProductKeyModel productKeyModel) {
		sessionFactory.getCurrentSession().update(productKeyModel);
	}

	@Override
	public void delete(ProductKeyModel productKeyModel) {
		sessionFactory.getCurrentSession().delete(productKeyModel);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductKeyModel> getAllProductKeys() {
		return sessionFactory.getCurrentSession()
				.createCriteria(ProductKeyModel.class).list();
	}


	// HibernateTemplate template;
	// public void setTemplate(HibernateTemplate template) {
	// this.template = template;
	// }

	// method to return one ProductKeyModel of given id
	// public ProductKeyModel getById(int id) {
	// ProductKeyModel ptKey = (ProductKeyModel) template.get(ProductKeyModel.class, id);
	// return ptKey;
	// }
	//
	// // method to save ProductKeyModel
	// public void saveProductKey(ProductKeyModel e) {
	// template.save(e);
	// }
	//
	// // method to update ProductKeyModel
	// public void updateProductKey(ProductKeyModel e) {
	// template.update(e);
	// }
	//
	// // method to delete ProductKeyModel
	// public void deleteProductKey(ProductKeyModel e) {
	// template.delete(e);
	// }
	//
	// // method to return all ProductKeys
	// public List<ProductKeyModel> getAllProductKey() {
	// List<ProductKeyModel> list = new ArrayList<ProductKeyModel>();
	// list = template.loadAll(ProductKeyModel.class);
	// return list;
	// }

	// public static DaoStatus CreateProductKey(ProductKeyModel prodKey) {
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

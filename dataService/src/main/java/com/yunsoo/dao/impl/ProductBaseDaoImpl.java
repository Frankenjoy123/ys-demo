package com.yunsoo.dao.impl;

import java.util.List;

import com.yunsoo.dao.ProductBaseDao;
import com.yunsoo.dbmodel.ProductBaseModel;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("productBaseDao")
@Transactional
public class ProductBaseDaoImpl implements ProductBaseDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ProductBaseModel getById(int id) {
        return (ProductBaseModel) sessionFactory.getCurrentSession().get(
                ProductBaseModel.class, id);
    }

    @Override
    public void save(ProductBaseModel productBaseModel) {
        sessionFactory.getCurrentSession().save(productBaseModel);
    }

    @Override
    public void update(ProductBaseModel productBaseModel) {
        sessionFactory.getCurrentSession().update(productBaseModel);
    }

    @Override
    public void delete(ProductBaseModel productBaseModel) {
        sessionFactory.getCurrentSession().delete(productBaseModel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProductBaseModel> getAllBaseProducts() {
        return sessionFactory.getCurrentSession()
                .createCriteria(ProductBaseModel.class).list();
    }

}

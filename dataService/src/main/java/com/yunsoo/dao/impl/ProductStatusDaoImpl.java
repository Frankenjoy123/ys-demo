package com.yunsoo.dao.impl;

import com.yunsoo.dao.ProductStatusDao;
import com.yunsoo.dbmodel.ProductStatusModel;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Zhe on 2015/1/13.
 */
@Repository("productStatusDao")
@Transactional
public class ProductStatusDaoImpl implements ProductStatusDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ProductStatusModel getById(int id) {
        return (ProductStatusModel) sessionFactory.getCurrentSession().get(
                ProductStatusModel.class, id);
    }

    @Override
    public void save(ProductStatusModel productStatusModel) {
        sessionFactory.getCurrentSession().save(productStatusModel);
    }

    @Override
    public void update(ProductStatusModel productStatusModel) {
        sessionFactory.getCurrentSession().update(productStatusModel);
    }

    @Override
    public void delete(ProductStatusModel productStatusModel) {
        sessionFactory.getCurrentSession().delete(productStatusModel);
    }

    @Override
    public List<ProductStatusModel> getAllProductKeyStatues(boolean activeOnly) {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(ProductStatusModel.class);
        if (activeOnly) {
            criteria.add(Restrictions.eq("active", activeOnly));
        }
        return criteria.list();
    }
}

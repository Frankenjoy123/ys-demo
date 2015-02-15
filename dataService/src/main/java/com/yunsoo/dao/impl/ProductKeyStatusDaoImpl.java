package com.yunsoo.dao.impl;

import com.yunsoo.dao.ProductKeyStatusDao;
import com.yunsoo.dbmodel.ProductKeyStatusModel;
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
@Repository("productKeyStatusDao")
@Transactional
public class ProductKeyStatusDaoImpl implements ProductKeyStatusDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ProductKeyStatusModel getById(int id) {
        return (ProductKeyStatusModel) sessionFactory.getCurrentSession().get(
                ProductKeyStatusModel.class, id);
    }

    @Override
    public void save(ProductKeyStatusModel productKeyStatusModel) {
        sessionFactory.getCurrentSession().save(productKeyStatusModel);
    }

    @Override
    public void update(ProductKeyStatusModel productKeyStatusModel) {
        sessionFactory.getCurrentSession().update(productKeyStatusModel);
    }

    @Override
    public void delete(ProductKeyStatusModel productKeyStatusModel) {
        sessionFactory.getCurrentSession().delete(productKeyStatusModel);
    }

    @Override
    public List<ProductKeyStatusModel> getAllProductKeyStatues(boolean activeOnly) {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(ProductKeyStatusModel.class);
        if (activeOnly) {
            criteria.add(Restrictions.eq("active", activeOnly));
        }
        return criteria.list();
    }
}

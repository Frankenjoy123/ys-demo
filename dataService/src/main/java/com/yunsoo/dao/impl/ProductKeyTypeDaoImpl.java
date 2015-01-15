package com.yunsoo.dao.impl;

import com.yunsoo.dao.ProductKeyTypeDao;
import com.yunsoo.dbmodel.ProductKeyType;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Zhe on 2015/1/13.
 */
@Repository("productKeyTypeDao")
@Transactional
public class ProductKeyTypeDaoImpl implements ProductKeyTypeDao{
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ProductKeyType getById(int id) {
        return (ProductKeyType)sessionFactory.getCurrentSession().get(
                ProductKeyType.class, id);
    }

    @Override
    public void save(ProductKeyType productKeyType) {
        sessionFactory.getCurrentSession().save(productKeyType);
    }

    @Override
    public void update(ProductKeyType productKeyType) {
        sessionFactory.getCurrentSession().update(productKeyType);
    }

    @Override
    public void delete(ProductKeyType productKeyType) {
        sessionFactory.getCurrentSession().delete(productKeyType);
    }

    @Override
    public List<ProductKeyType> getAllProductKeyType(boolean activeOnly) {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(ProductKeyType.class);
        if (activeOnly) {
            criteria.add(Restrictions.eq("active", activeOnly));
        }
        return criteria.list();
    }
}

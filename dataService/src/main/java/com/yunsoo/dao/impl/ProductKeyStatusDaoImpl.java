package com.yunsoo.dao.impl;

import com.yunsoo.dao.ProductKeyStatusDao;
import com.yunsoo.dbmodel.ProductKeyStatus;
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
    public ProductKeyStatus getById(int id) {
        return (ProductKeyStatus) sessionFactory.getCurrentSession().get(
                ProductKeyStatus.class, id);
    }

    @Override
    public void save(ProductKeyStatus productKeyStatus) {
        sessionFactory.getCurrentSession().save(productKeyStatus);
    }

    @Override
    public void update(ProductKeyStatus productKeyStatus) {
        sessionFactory.getCurrentSession().update(productKeyStatus);
    }

    @Override
    public void delete(ProductKeyStatus productKeyStatus) {
        sessionFactory.getCurrentSession().delete(productKeyStatus);
    }

    @Override
    public List<ProductKeyStatus> getAllProductKeyStatues(boolean activeOnly) {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(ProductKeyStatus.class);
        if (activeOnly) {
            criteria.add(Restrictions.eq("active", activeOnly));
        }
        return criteria.list();
    }
}

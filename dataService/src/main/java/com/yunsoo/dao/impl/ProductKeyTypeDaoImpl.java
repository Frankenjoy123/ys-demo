package com.yunsoo.dao.impl;

import com.yunsoo.dao.ProductKeyTypeDao;
import com.yunsoo.dbmodel.ProductKeyTypeModel;
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
    public ProductKeyTypeModel getById(int id) {
        return (ProductKeyTypeModel) sessionFactory.getCurrentSession().get(
                ProductKeyTypeModel.class, id);
    }

    @Override
    public void save(ProductKeyTypeModel productKeyTypeModel) {
        sessionFactory.getCurrentSession().save(productKeyTypeModel);
    }

    @Override
    public void update(ProductKeyTypeModel productKeyTypeModel) {
        sessionFactory.getCurrentSession().update(productKeyTypeModel);
    }

    @Override
    public void delete(ProductKeyTypeModel productKeyTypeModel) {
        sessionFactory.getCurrentSession().delete(productKeyTypeModel);
    }

    @Override
    public List<ProductKeyTypeModel> getAllProductKeyType(boolean activeOnly) {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(ProductKeyTypeModel.class);
        if (activeOnly) {
            criteria.add(Restrictions.eq("active", activeOnly));
        }
        return criteria.list();
    }
}

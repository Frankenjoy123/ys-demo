package com.yunsoo.dao.impl;

import com.yunsoo.dao.ProductKeyBatchDao;
import com.yunsoo.dbmodel.ProductKeyBatchModel;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
@Repository("productKeyBatchDao")
@Transactional
public class ProductKeyBatchDaoImpl implements ProductKeyBatchDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ProductKeyBatchModel getById(Long id) {
        return (ProductKeyBatchModel) sessionFactory.getCurrentSession().get(ProductKeyBatchModel.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductKeyBatchModel> getByFilterPaged(Map<String, Object> eqFilter, int pageIndex, int pageSize) {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductKeyBatchModel.class);
        if (eqFilter != null && !eqFilter.isEmpty()) {
            eqFilter.forEach((k, v) -> {
                if (!StringUtils.isEmpty(k)) {
                    c.add(Restrictions.eqOrIsNull(k, v));
                }
            });
        }
        c.setFirstResult(pageIndex * pageSize);
        c.setMaxResults(pageSize);
        return c.list();
    }

    @Override
    public void save(ProductKeyBatchModel productKeyModel) {
        sessionFactory.getCurrentSession().save(productKeyModel);
    }

    @Override
    public void update(ProductKeyBatchModel productKeyModel) {
        sessionFactory.getCurrentSession().update(productKeyModel);
    }
}

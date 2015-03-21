package com.yunsoo.dao.impl;

import com.yunsoo.dao.ProductKeyBatchStatusDao;
import com.yunsoo.dbmodel.ProductKeyBatchStatusModel;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/11
 * Descriptions:
 */
@Repository("productKeyBatchStatusDao")
@Transactional
public class ProductKeyBatchStatusDaoImpl implements ProductKeyBatchStatusDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<ProductKeyBatchStatusModel> getAll(boolean activeOnly) {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(ProductKeyBatchStatusModel.class);
        if (activeOnly) {
            criteria.add(Restrictions.eq("active", true));
        }
        return criteria.list();
    }
}

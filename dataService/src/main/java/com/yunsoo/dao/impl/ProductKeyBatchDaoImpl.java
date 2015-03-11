package com.yunsoo.dao.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.yunsoo.dao.ProductKeyBatchDao;
import com.yunsoo.dbmodel.ProductKeyBatchModel;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
@Repository("productKeyBatchDao")
public class ProductKeyBatchDaoImpl implements ProductKeyBatchDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ProductKeyBatchModel getById(int id) {
        return (ProductKeyBatchModel) sessionFactory.getCurrentSession().get(ProductKeyBatchModel.class, id);
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

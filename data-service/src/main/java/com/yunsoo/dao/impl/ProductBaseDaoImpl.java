package com.yunsoo.dao.impl;

import java.util.List;
import java.util.Map;

import com.yunsoo.dao.ProductBaseDao;
import com.yunsoo.dbmodel.ProductBaseModel;
import com.yunsoo.util.SpringBeanUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Repository("productBaseDao")
@Transactional
public class ProductBaseDaoImpl implements ProductBaseDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ProductBaseModel getById(long id) {
        return (ProductBaseModel) sessionFactory.getCurrentSession().get(ProductBaseModel.class, id);
    }


    @Override
    public void save(ProductBaseModel productBaseModel) {
        sessionFactory.getCurrentSession().save(productBaseModel);
    }

    @Override
    public void update(ProductBaseModel productBaseModel) {
        sessionFactory.getCurrentSession().update(productBaseModel);
    }

    //patch update model for intended updated properties.
    @Override
    public void patchUpdate(ProductBaseModel productBaseModelForPatch) {
        Session currentSession = sessionFactory.getCurrentSession();
        ProductBaseModel modelInDB = (ProductBaseModel) currentSession.get(ProductBaseModel.class, productBaseModelForPatch.getId());
        if (modelInDB == null) {
            throw new RuntimeException("product base not found"); //todo
        }
        //Set properties that needs to update in DB, ignore others are null.
        BeanUtils.copyProperties(productBaseModelForPatch, modelInDB, SpringBeanUtil.getNullPropertyNames(productBaseModelForPatch));
        currentSession.update(modelInDB);
    }

    @Override
    public void delete(ProductBaseModel productBaseModel) {
        sessionFactory.getCurrentSession().delete(productBaseModel);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductBaseModel> getByFilter(Map<String, Object> eqFilter) {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductBaseModel.class);
        if (eqFilter != null && !eqFilter.isEmpty()) {
            eqFilter.forEach((k, v) -> {
                if (!StringUtils.isEmpty(k)) {
                    c.add(Restrictions.eq(k, v));
                }
            });
        }
        return c.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductBaseModel> getAll() {
        return sessionFactory.getCurrentSession().createCriteria(ProductBaseModel.class).list();
    }

}

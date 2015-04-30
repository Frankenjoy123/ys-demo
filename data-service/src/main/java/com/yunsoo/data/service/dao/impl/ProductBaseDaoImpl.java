package com.yunsoo.data.service.dao.impl;

import java.util.List;
import java.util.Map;

import com.yunsoo.data.service.dao.ProductBaseDao;
import com.yunsoo.data.service.dbmodel.ProductBaseModel;
import com.yunsoo.data.service.util.SpringBeanUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Repository("productBaseDao")
@Transactional
public class ProductBaseDaoImpl implements ProductBaseDao {

    @Autowired
    @Qualifier(value = "sessionfactory.primary")
    private SessionFactory sessionFactory;

    @Autowired
    @Qualifier(value = "sessionfactory.read1")
    private SessionFactory readSessionFactory;

    @Override
    public ProductBaseModel getById(String id) {
        return (ProductBaseModel) readSessionFactory.getCurrentSession().get(ProductBaseModel.class, id);
    }


    @Override
    public String save(ProductBaseModel productBaseModel) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(productBaseModel);
        return productBaseModel.getId();
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

//    @Override
//    public List<ProductBaseModel> getByFilter(Map<String, Object> eqFilter) {
//        Criteria c = readSessionFactory.getCurrentSession().createCriteria(ProductBaseModel.class);
//        if (eqFilter != null && !eqFilter.isEmpty()) {
//            eqFilter.forEach((k, v) -> {
//                if (!StringUtils.isEmpty(k)) {
//                    c.add(Restrictions.eqOrIsNull(k, v));
//                }
//            });
//        }
//        return c.list();
//    }

    @Override
    public List<ProductBaseModel> getByFilter(Map<String, Object> eqFilter, Map<String, List<String>> notEqFilter) {
        Criteria c = readSessionFactory.getCurrentSession().createCriteria(ProductBaseModel.class);
        if (eqFilter != null && !eqFilter.isEmpty()) {
            eqFilter.forEach((k, v) -> {
                if (!StringUtils.isEmpty(k)) {
                    c.add(Restrictions.eqOrIsNull(k, v));
                }
            });
        }
        if (notEqFilter != null && !notEqFilter.isEmpty()) {
            notEqFilter.forEach((k, v) -> {
                if (!StringUtils.isEmpty(k)) {
                    v.forEach((s) -> {
                        c.add(Restrictions.ne(k, s));
                    });
                }
            });
        }
        return c.list();
    }

    @Override
    public List<ProductBaseModel> getAll() {
        return readSessionFactory.getCurrentSession().createCriteria(ProductBaseModel.class).list();
    }

}

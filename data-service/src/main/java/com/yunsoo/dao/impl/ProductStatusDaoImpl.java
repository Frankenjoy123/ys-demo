package com.yunsoo.dao.impl;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.ProductStatusDao;
import com.yunsoo.dbmodel.ProductStatusModel;
import com.yunsoo.util.SpringBeanUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/1/13
 * Descriptions:
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
    public int save(ProductStatusModel productStatusModel) {
        sessionFactory.getCurrentSession().save(productStatusModel);
        return productStatusModel.getId();
    }

    //Dynamic update model for intended updated properties.
    @Override
    public DaoStatus patchUpdate(ProductStatusModel productStatusModelForPatch) {
        try {
            Session currentSession = sessionFactory.getCurrentSession();
            ProductStatusModel modelInDB = (ProductStatusModel) currentSession.get(ProductStatusModel.class, productStatusModelForPatch.getId());
            //Set properties that needs to update in DB, ignore others are null.
            BeanUtils.copyProperties(productStatusModelForPatch, modelInDB, SpringBeanUtil.getNullPropertyNames(productStatusModelForPatch));
            currentSession.update(modelInDB);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log
            System.out.println(ex.getStackTrace());
            System.out.println(ex.getMessage());
            return DaoStatus.fail;
        }
    }

    @Override
    public DaoStatus update(ProductStatusModel productStatusModel) {
        try {
            sessionFactory.getCurrentSession().update(productStatusModel);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log
            return DaoStatus.fail;
        }
    }

    @Override
    public void delete(ProductStatusModel productStatusModel) {
        sessionFactory.getCurrentSession().delete(productStatusModel);
    }

    @Override
    public DaoStatus delete(int id) {

        return this.update(id, false);
    }

    private DaoStatus update(int id, Boolean isActive) {
        ProductStatusModel productStatusModel = this.getById(id);
        if (productStatusModel == null) return DaoStatus.NotFound;

        productStatusModel.setActive(isActive);
        this.update(productStatusModel);
        return DaoStatus.success;
    }

    @Override
    public List<ProductStatusModel> getAll(boolean active) {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(ProductStatusModel.class);
        if (active) {
            criteria.add(Restrictions.eq("active", true));
        }
        return criteria.list();
    }
}

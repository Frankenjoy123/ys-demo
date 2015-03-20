package com.yunsoo.dao.impl;

import java.util.List;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.ProductBaseDao;
import com.yunsoo.dbmodel.ProductBaseModel;
import com.yunsoo.util.SpringBeanUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("productBaseDao")
@Transactional
public class ProductBaseDaoImpl implements ProductBaseDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ProductBaseModel getById(long id) {
        return (ProductBaseModel) sessionFactory.getCurrentSession().get(
                ProductBaseModel.class, id);
    }

    @Override
    public List<ProductBaseModel> getProductBaseByFilter(Integer manufacturerId, Integer categoryId) {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductBaseModel.class);
        if (manufacturerId != null) {
            c.add(Restrictions.eq("manufacturerId", manufacturerId.intValue()));
        }
        if (categoryId != null) {
            c.add(Restrictions.eq("categoryId", categoryId.intValue()));
        }
        c.add(Restrictions.eq("active", true));
        return c.list();
    }

    @Override
    public void save(ProductBaseModel productBaseModel) {
        sessionFactory.getCurrentSession().save(productBaseModel);
    }

    //Dynamic update model for intended updated properties.
    @Override
    public DaoStatus patchUpdate(ProductBaseModel productBaseModelForPatch) {
        try {
            Session currentSession = sessionFactory.getCurrentSession();
            ProductBaseModel modelInDB = (ProductBaseModel) currentSession.get(ProductBaseModel.class, productBaseModelForPatch.getId());
            //Set properties that needs to update in DB, ignore others are null.
            BeanUtils.copyProperties(productBaseModelForPatch, modelInDB, SpringBeanUtil.getNullPropertyNames(productBaseModelForPatch));
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
    public void update(ProductBaseModel productBaseModel) {
        sessionFactory.getCurrentSession().update(productBaseModel);
    }

    @Override
    public void deletePermanently(ProductBaseModel productBaseModel) {
        sessionFactory.getCurrentSession().delete(productBaseModel);
    }

    @Override
    public DaoStatus delete(long Id) {
        //set as inactive
        return this.updateActiveFlag(Id, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProductBaseModel> getAllProductsBase() {
        return sessionFactory.getCurrentSession()
                .createCriteria(ProductBaseModel.class).list();
    }

    //Update active flag.
    public DaoStatus updateActiveFlag(long Id, Boolean activeFlag) {
        ProductBaseModel productBaseModel = this.getById(Id);
        if (productBaseModel == null) return DaoStatus.NotFound;

        productBaseModel.setActive(activeFlag);
        this.patchUpdate(productBaseModel);
        return DaoStatus.success;
    }

}

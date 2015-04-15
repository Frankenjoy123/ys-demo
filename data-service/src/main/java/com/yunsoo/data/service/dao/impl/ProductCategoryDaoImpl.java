package com.yunsoo.data.service.dao.impl;

import java.util.List;

import com.yunsoo.data.service.dbmodel.ProductCategoryModel;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.yunsoo.data.service.dao.ProductCategoryDao;

@Repository("productCategoryDao")
@Transactional
public class ProductCategoryDaoImpl implements ProductCategoryDao {

    @Autowired
    private SessionFactory sessionFactory;
    private int rootCategoryParentId = -1; //hardcode in database

    @Override
    public ProductCategoryModel getById(int id) {
        return (ProductCategoryModel) sessionFactory.getCurrentSession().get(
                ProductCategoryModel.class, id);
    }

    @Override
    //getById product-sub-categories by parent category.
    public List<ProductCategoryModel> getProductCategoriesByParentId(int parentId) {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductCategoryModel.class)
                .add(Restrictions.eq("parentId", parentId))
                .add(Restrictions.eq("active", true))
                .addOrder(Order.asc("id"));
        return c.list();
    }

    @Override
    public List<ProductCategoryModel> getRootProductCategories() {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductCategoryModel.class)
                .add(Restrictions.eq("parentId", rootCategoryParentId)) //hardcode in DB.
                .add(Restrictions.eq("active", true))
                .addOrder(Order.asc("id"));
        return c.list();
    }

    @Override
    public void save(ProductCategoryModel productCategoryModelModel) {
        sessionFactory.getCurrentSession().save(productCategoryModelModel);
    }

    @Override
    public void update(ProductCategoryModel productCategoryModelModel) {
        sessionFactory.getCurrentSession().update(productCategoryModelModel);
    }

    @Override
    public void delete(ProductCategoryModel productCategoryModelModel) {
        sessionFactory.getCurrentSession().delete(productCategoryModelModel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProductCategoryModel> getAllProductCategories() {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductCategoryModel.class)
                .add(Restrictions.eq("active", true))
                .addOrder(Order.asc("id"));
        return c.list();

//        return sessionFactory.getCurrentSession()
//                .createCriteria(ProductCategoryModel.class).list();
    }

}

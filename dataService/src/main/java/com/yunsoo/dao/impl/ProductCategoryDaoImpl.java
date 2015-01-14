package com.yunsoo.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Projections;

import com.yunsoo.dao.ProductCategoryDao;
import com.yunsoo.model.ProductCategory;

@Repository("productCategoryDao")
@Transactional
public class ProductCategoryDaoImpl implements ProductCategoryDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ProductCategory getById(int id) {
        return (ProductCategory) sessionFactory.getCurrentSession().get(
                ProductCategory.class, id);
    }

    @Override
    //get product-sub-categories by parent category.
    public List<ProductCategory> getProductCategoriesByParentId(int parentId) {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductCategory.class)
                .add(Restrictions.eq("parentId", parentId))
                .addOrder(Order.asc("id"));
        return c.list();

//        return (ProductCategory) sessionFactory.getCurrentSession().get(
//                ProductCategory.class, parentId);
    }

    @Override
    public List<ProductCategory> getRootProductCategories() {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductCategory.class)
                .add(Restrictions.eq("parentId",-1)) //hardcode in DB.
                .addOrder(Order.asc("id"));
        return c.list();
    }

    @Override
    public void save(ProductCategory productCategory) {
        sessionFactory.getCurrentSession().save(productCategory);
    }

    @Override
    public void update(ProductCategory productCategory) {
        sessionFactory.getCurrentSession().update(productCategory);
    }

    @Override
    public void delete(ProductCategory productCategory) {
        sessionFactory.getCurrentSession().delete(productCategory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProductCategory> getAllProductCategories() {
        return sessionFactory.getCurrentSession()
                .createCriteria(ProductCategory.class).list();
    }

}

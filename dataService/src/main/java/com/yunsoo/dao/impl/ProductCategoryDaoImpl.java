package com.yunsoo.dao.impl;

import java.util.List;

import com.yunsoo.dbmodel.ProductCategoryModel;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.yunsoo.dao.ProductCategoryDao;

@Repository("productCategoryDao")
@Transactional
public class ProductCategoryDaoImpl implements ProductCategoryDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ProductCategoryModel getById(int id) {
        return (ProductCategoryModel) sessionFactory.getCurrentSession().get(
                ProductCategoryModel.class, id);
    }

    @Override
    //get product-sub-categories by parent category.
    public List<ProductCategoryModel> getProductCategoriesByParentId(int parentId) {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductCategoryModel.class)
                .add(Restrictions.eq("parentId", parentId))
                .addOrder(Order.asc("id"));
        return c.list();

//        return (ProductCategoryModel) sessionFactory.getCurrentSession().get(
//                ProductCategoryModel.class, parentId);
    }

    @Override
    public List<ProductCategoryModel> getRootProductCategories() {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductCategoryModel.class)
                .add(Restrictions.eq("parentId",-1)) //hardcode in DB.
                .addOrder(Order.asc("id"));
        return c.list();
    }

    @Override
    public void save(ProductCategoryModel productCategoryModel) {
        sessionFactory.getCurrentSession().save(productCategoryModel);
    }

    @Override
    public void update(ProductCategoryModel productCategoryModel) {
        sessionFactory.getCurrentSession().update(productCategoryModel);
    }

    @Override
    public void delete(ProductCategoryModel productCategoryModel) {
        sessionFactory.getCurrentSession().delete(productCategoryModel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProductCategoryModel> getAllProductCategories() {
        return sessionFactory.getCurrentSession()
                .createCriteria(ProductCategoryModel.class).list();
    }

}

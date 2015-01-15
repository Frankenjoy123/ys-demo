package com.yunsoo.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yunsoo.dao.ProductCategoryDao;
import com.yunsoo.dbmodel.ProductCategory;
import com.yunsoo.service.ProductCategoryService;


@Service("productCategoryService")
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Override
    public ProductCategory getById(int id) {
        return productCategoryDao.getById(id);
    }

    @Override
    public void save(ProductCategory productCategory) {
        productCategoryDao.save(productCategory);
    }

    @Override
    public void update(ProductCategory productCategory) {
        productCategoryDao.update(productCategory);
    }

    @Override
    public void delete(ProductCategory productCategory) {
        productCategoryDao.delete(productCategory);
    }

    @Transactional
    @Override
    public List<ProductCategory> getAllProductCategories() {
        return productCategoryDao.getAllProductCategories();
    }

    @Override
    @Transactional
    public List<ProductCategory> getProductCategoriesByParentId(int parentId) {
        return productCategoryDao.getProductCategoriesByParentId(parentId);
    }

    @Override
    @Transactional
    public List<ProductCategory> getRootProductCategories() {
        return productCategoryDao.getRootProductCategories();
    }
}

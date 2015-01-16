package com.yunsoo.service.Impl;

import java.util.List;

import com.yunsoo.dbmodel.ProductCategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yunsoo.dao.ProductCategoryDao;
import com.yunsoo.service.ProductCategoryService;


@Service("productCategoryService")
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Override
    public ProductCategoryModel getById(int id) {
        return productCategoryDao.getById(id);
    }

    @Override
    public void save(ProductCategoryModel productCategoryModel) {
        productCategoryDao.save(productCategoryModel);
    }

    @Override
    public void update(ProductCategoryModel productCategoryModel) {
        productCategoryDao.update(productCategoryModel);
    }

    @Override
    public void delete(ProductCategoryModel productCategoryModel) {
        productCategoryDao.delete(productCategoryModel);
    }

    @Transactional
    @Override
    public List<ProductCategoryModel> getAllProductCategories() {
        return productCategoryDao.getAllProductCategories();
    }

    @Override
    @Transactional
    public List<ProductCategoryModel> getProductCategoriesByParentId(int parentId) {
        return productCategoryDao.getProductCategoriesByParentId(parentId);
    }

    @Override
    @Transactional
    public List<ProductCategoryModel> getRootProductCategories() {
        return productCategoryDao.getRootProductCategories();
    }
}

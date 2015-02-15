package com.yunsoo.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yunsoo.dao.ProductCategoryDao;
import com.yunsoo.service.ProductCategoryService;
import com.yunsoo.service.contract.ProductCategory;

@Service("productCategoryService")
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Override
    public ProductCategory getById(int id) {
        return ProductCategory.FromModel(productCategoryDao.getById(id));
    }

    @Override
    public void save(ProductCategory pc) {
        productCategoryDao.save(ProductCategory.ToModel(pc));
    }

    @Override
    public void update(ProductCategory pc) {

        productCategoryDao.update(ProductCategory.ToModel(pc));
    }

    @Override
    public void delete(ProductCategory pc) {
        productCategoryDao.delete(ProductCategory.ToModel(pc));
    }

    @Transactional
    @Override
    public List<ProductCategory> getAllProductCategories() {
        return ProductCategory.FromModelList(productCategoryDao.getAllProductCategories());
    }

    @Override
    @Transactional
    public List<ProductCategory> getProductCategoriesByParentId(int parentId) {
        return ProductCategory.FromModelList(productCategoryDao.getProductCategoriesByParentId(parentId));
    }

    @Override
    @Transactional
    public List<ProductCategory> getRootProductCategories() {
        return ProductCategory.FromModelList(productCategoryDao.getRootProductCategories());
    }
}

package com.yunsoo.service.Impl;

import com.yunsoo.dao.ProductDao;
import com.yunsoo.dao.ProductPackageDao;
import com.yunsoo.dao.impl.ProductDaoImpl;
import com.yunsoo.dbmodel.ProductCategoryModel;
import com.yunsoo.dbmodel.ProductPackageModel;
import com.yunsoo.service.ProductPackageService;
import com.yunsoo.service.contract.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by: Lijian Created on: 2015/2/1 Descriptions:
 */
@Service("ProductPackageService")
public class ProductPackageServiceImpl implements ProductPackageService {

    @Autowired
    private ProductPackageDao packageDao;

    @Autowired
    private ProductDao productDao;

    /**
     * Only one level once. We do not recursively load the packages.
     *
     * @param key
     * @return
     */
    @Override
    public ProductPackage list(String key) {

        ProductPackageModel model = packageDao.getByKey(key);
        if (model == null) {
            throw new IllegalArgumentException("This key is not a valid package key.");
        }
        ProductPackage p = new ProductPackage(model);

        Set<String> subKeys = model.getChildProductKeySet();

        if (subKeys != null) {

            List<ProductPackageModel> subModels = packageDao.batchLoad(subKeys);
            if (subModels != null && subModels.size() > 0)//they are packages.
            {
                p.setPackageCount(subModels.size());
                List<ProductPackage> subPackages = new ArrayList<ProductPackage>();
                for (ProductPackageModel subModel : subModels) {
                    subPackages.add(new ProductPackage(subModel));
                }
                p.setSubPackages(subPackages);
            } else {
                //TODO: batch load product from productDao.

                List<Product> products = new ArrayList<Product>();
                for (String productKey : subKeys) {
                    Product product = new Product();
                    product.setId(productKey);
                    products.add(product);
                }
                p.setProducts(products);
                p.setProductCount(subKeys.size());
            }

        }
        return p;

    }

    @Override
    public boolean bind(String packageKey, List<String> subKeys, long operator) {
        ProductPackageModel model = new ProductPackageModel();
        model.setProductKey(packageKey);
        model.setChildProductKeySet(new HashSet<>(subKeys));
        model.setParentProductKey(null);//currently not know
        model.setCreatedDateTime(new DateTime());
        model.setOperator(operator);
        packageDao.save(model);

        //TODO save parent key if possibly.
        return true;
    }

    @Override
    public boolean revoke(String key) {
        ProductPackageModel model = packageDao.getByKey(key);
        if (model == null) {
            throw new IllegalArgumentException("This key is not a valid package key.");
        }

        Set<String> subKeys = model.getChildProductKeySet();

        List<ProductPackageModel> subModels = packageDao.batchLoad(subKeys);
        if (subModels != null && !subModels.isEmpty()) {
            for (ProductPackageModel subModel : subModels) {
                subModel.setParentProductKey(null);
            }
        }
        model.setChildProductKeySet(new HashSet<>());
        model.setStatusId(1); //0 new, 1 revoke

        packageDao.save(model);
        packageDao.batchSave(new HashSet<>(subModels));
        return true;

    }

    @Override
    public boolean revoke(String key, List<String> revokeKeys) {
        ProductPackageModel model = packageDao.getByKey(key);
        if (model == null) {
            throw new IllegalArgumentException("This key is not a valid package key.");
        }

        for (String subKey : revokeKeys) {
            if (model.getChildProductKeySet().contains(subKey)) {
                ProductPackageModel subModel = packageDao.getByKey(subKey);
                if (subModel != null) {
                    revoke(subKey);
                }
                model.getChildProductKeySet().remove(subKey);
            }
        }
        if (model.getChildProductKeySet().isEmpty()) {
            model.setStatusId(1);
        }

        packageDao.save(model);

        return true;

    }

}

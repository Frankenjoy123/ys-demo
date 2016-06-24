package com.yunsoo.data.service.service.Impl;


import com.yunsoo.data.service.dao.ProductPackageDao;
import com.yunsoo.data.service.dbmodel.dynamodb.ProductPackageModel;
import com.yunsoo.data.service.service.ProductPackageService;
import com.yunsoo.data.service.service.contract.ProductPackage;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by: Lijian
 * Created on: 2015/2/1
 * Descriptions:
 */
@Service("ProductPackageService")
public class ProductPackageServiceImpl implements ProductPackageService {

    @Autowired
    private ProductPackageDao productPackageDao;


    @Override
    public ProductPackage getByKey(String key) {
        ProductPackageModel productPackageModel = findByKey(key);
        return productPackageModel != null ? new ProductPackage(productPackageModel) : null;
    }

    @Override
    public Set<String> getAllChildProductKeySetByKey(String key) {
        Set<String> keySet = new HashSet<>();
        ProductPackageModel productPackageModel = findByKey(key);
        if (productPackageModel != null) {
            keySet.add(key);
            Set<String> childProductKeySet = productPackageModel.getChildProductKeySet();
            if (childProductKeySet != null && childProductKeySet.size() > 0) {
                for (String ck : childProductKeySet) {
                    if (!keySet.contains(ck)) {
                        keySet.addAll(getAllChildProductKeySetByKey(ck));
                    }
                }
            }
        }
        return keySet;
    }

    @Override
    public void disable(String key) {
        ProductPackageModel productPackageModel = productPackageDao.getByKey(key);
        if (productPackageModel != null) {
            productPackageModel.setDisabled(true);
            productPackageDao.save(productPackageModel);
        }
    }

    @Override
    public void save(ProductPackage productPackage) {
        productPackageDao.batchSave(toProductPackageModels(productPackage, null).values());
    }

    @Override
    public int batchPackage(List<ProductPackage> packages) {
        int count = 0;
        if (packages == null || packages.size() == 0) {
            return count;
        }

        packages.stream().sorted((p1, p2) -> Long.compare(
                (p1 == null || p1.getPackageDateTime() == null) ? 0 : p1.getPackageDateTime().getMillis(),
                (p2 == null || p2.getPackageDateTime() == null) ? 0 : p2.getPackageDateTime().getMillis()));

        Map<String, ProductPackageModel> cacheMap = new HashMap<>();
        for (ProductPackage p : packages) {
            toProductPackageModels(p, cacheMap);
            if (p != null && cacheMap.containsKey(p.getProductKey())) {
                count++;
            }
        }

        productPackageDao.batchSave(cacheMap.values());
        return count;
    }

    private ProductPackageModel findByKey(String key) {
        ProductPackageModel productPackageModel = productPackageDao.getByKey(key);
        if (productPackageModel != null && !productPackageModel.isDisabled()) {
            return productPackageModel;
        }
        return null;
    }

    private Map<String, ProductPackageModel> toProductPackageModels(ProductPackage productPackage, Map<String, ProductPackageModel> cacheMap) {
        if (cacheMap == null) {
            cacheMap = new HashMap<>();
        }
        if (productPackage == null) {
            return cacheMap;
        }
        String productKey = productPackage.getProductKey();
        String parentProductKey = productPackage.getParentProductKey();
        Set<String> childProductKeySet = productPackage.getChildProductKeySet();
        DateTime packageDateTime = productPackage.getPackageDateTime() != null ? productPackage.getPackageDateTime() : DateTime.now();
        boolean hasParent = parentProductKey != null && parentProductKey.length() > 0;
        boolean hasChild = childProductKeySet != null && childProductKeySet.size() > 0;
        if (productKey == null || productKey.length() == 0 || !(hasParent || hasChild)) {
            return cacheMap;
        }

        ProductPackageModel current = fixNullOrDisabled(productKey, getByKeyCached(productKey, cacheMap));
        boolean currentHasChanged = false;
        if (hasParent) {
            //update current
            current.setParentProductKey(parentProductKey);
            currentHasChanged = true;

            //update parent
            ProductPackageModel parent = fixNullOrDisabled(parentProductKey, getByKeyCached(parentProductKey, cacheMap));
            parent.appendChildProductKey(productKey);
            parent.setPackageDateTime(packageDateTime);
            cacheMap.put(parentProductKey, parent);
        }

        if (hasChild && current.canBeOverrideOn(packageDateTime)) {
            //update current
            current.appendChildProductKey(childProductKeySet);
            currentHasChanged = true;

            //update child
            for (String ck : childProductKeySet) {
                if (ck != null && ck.length() > 0) {
                    ProductPackageModel cm = fixNullOrDisabled(ck, getByKeyCached(ck, cacheMap));
                    cm.setParentProductKey(productKey);
                    cacheMap.put(ck, cm);
                }
            }
        }

        if (currentHasChanged) {
            current.setPackageDateTime(packageDateTime);
            cacheMap.put(productKey, current);
        }

        return cacheMap;
    }

    private ProductPackageModel getByKeyCached(String key, Map<String, ProductPackageModel> cacheMap) {
        ProductPackageModel model = cacheMap != null ? cacheMap.get(key) : null;
        return model != null ? model : productPackageDao.getByKey(key);
    }

    private ProductPackageModel fixNullOrDisabled(String key, ProductPackageModel model) {
        if (model == null || model.isDisabled()) {
            model = new ProductPackageModel();
            model.setProductKey(key);
        }
        return model;
    }

}

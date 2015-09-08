package com.yunsoo.data.service.dao;

import com.yunsoo.data.service.dbmodel.dynamodb.ProductPackageModel;

import java.util.List;
import java.util.Set;


/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
public interface ProductPackageDao {

    ProductPackageModel getByKey(String key);

    DaoStatus save(ProductPackageModel productKeyModel);

    DaoStatus update(ProductPackageModel productKeyModel);
    
    List<ProductPackageModel> batchLoad(Set<String> keys);
    
    DaoStatus batchSave(List<ProductPackageModel> packages);
    
            
}

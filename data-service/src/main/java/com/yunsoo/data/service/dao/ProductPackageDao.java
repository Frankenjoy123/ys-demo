package com.yunsoo.data.service.dao;

import com.yunsoo.data.service.dbmodel.ProductPackageModel;

import java.util.List;
import java.util.Set;


/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
public interface ProductPackageDao {

    public ProductPackageModel getByKey(String key);

    public DaoStatus save(ProductPackageModel productKeyModel);

    public DaoStatus update(ProductPackageModel productKeyModel);
    
    public List<ProductPackageModel> batchLoad(Set<String> keys);
    
    public DaoStatus batchSave(List<ProductPackageModel> packages);
    
            
}
package com.yunsoo.service;

import com.yunsoo.dbmodel.ProductPackageModel;
import com.yunsoo.service.contract.ProductPackage;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
public interface ProductPackageService {
    public ProductPackage list(String key);
    public boolean bind(String packageKey, List<String> subKeys, long operator);
    public boolean revoke(String key);
    public boolean revoke(String key, List<String> revokeKeys);    
}

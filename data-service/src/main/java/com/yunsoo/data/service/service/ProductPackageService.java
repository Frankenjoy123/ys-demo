package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.contract.PackageBoundContract;
import com.yunsoo.data.service.service.contract.PackageContract;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
public interface ProductPackageService {
    PackageContract query(String key);
    boolean bind(PackageBoundContract data);
    boolean batchBind(PackageBoundContract[] dataArray);
    boolean revoke(String key);
    List<String> loadAllKeys(String key);
}

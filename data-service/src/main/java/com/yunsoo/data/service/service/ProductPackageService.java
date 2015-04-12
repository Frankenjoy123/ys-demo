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
    public PackageContract query(String key);
    public boolean bind(PackageBoundContract data);
    public boolean batchBind(PackageBoundContract[] dataArray);
    public boolean revoke(String key);
    public List<String> loadAllKeys(String key);
}
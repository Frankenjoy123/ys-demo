package com.yunsoo.key.service;

import java.util.List;
import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2016-08-16
 * Descriptions:
 */
public interface PackageService {

    Package getByKey(String key);

    Set<String> getChildKeySet(String key);

    void disable(String key);

    void save(Package pkg);

    int batchSave(List<Package> packages);

}

package com.yunsoo.processor.key.service;

import com.yunsoo.processor.client.KeyApiClient;
import com.yunsoo.processor.key.dto.ProductPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-28
 * Descriptions:
 */
@Service
public class ProductPackageService {

    @Autowired
    private KeyApiClient keyApiClient;


    public int batchSave(List<ProductPackage> productPackages) {
        return keyApiClient.post("productPackage/batchSave", productPackages, int.class);
    }

}

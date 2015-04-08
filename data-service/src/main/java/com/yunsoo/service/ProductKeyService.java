package com.yunsoo.service;

import com.yunsoo.service.contract.Product;
import com.yunsoo.service.contract.ProductKey;

import java.util.List;

public interface ProductKeyService {

    ProductKey get(String key);

    void setDisabled(String key, Boolean disable);

    void batchSave(long productKeyBatchId,
                   List<Integer> productKeyTypeIds,
                   List<List<String>> productKeys,
                   Product productTemplate);

}

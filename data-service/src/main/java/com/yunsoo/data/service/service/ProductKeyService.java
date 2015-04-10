package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.contract.Product;
import com.yunsoo.data.service.service.contract.ProductKey;

import java.util.List;

public interface ProductKeyService {

    ProductKey get(String key);

    void setDisabled(String key, Boolean disable);

    void batchSave(long productKeyBatchId,
                   List<Integer> productKeyTypeIds,
                   List<List<String>> productKeys,
                   Product productTemplate);

}

package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.contract.Product;

public interface ProductService {

    Product getByKey(String productKey);

//    void batchCreate(Product productTemplate, List<String> productKeyList);

    void update(Product product);

    void patchUpdate(Product product);

}

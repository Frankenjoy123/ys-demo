package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.contract.Product;

public interface ProductService {

    Product getByKey(String productKey);

    void patchUpdate(Product product);

}

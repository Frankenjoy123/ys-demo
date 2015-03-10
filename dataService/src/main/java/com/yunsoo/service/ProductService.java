package com.yunsoo.service;

import com.yunsoo.service.contract.Product;

import java.util.List;

public interface ProductService {

    public Product getByKey(String productKey);

    public void batchCreate(Product productTemplate, List<String> productKeyList);

    public void update(Product product);

    public void patchUpdate(Product product);

}

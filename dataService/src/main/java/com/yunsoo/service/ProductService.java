package com.yunsoo.service;

import com.yunsoo.service.contract.Product;

import java.util.List;

public interface ProductService {

	public Product getByKey(String productKey);

    public void batchCreate(int baseProductId, List<String> productKeyList);

	public void active(String productKey);
}

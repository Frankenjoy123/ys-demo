package com.yunsoo.service;

import com.yunsoo.service.contract.ProductKey;

public interface ProductKeyService {

    public ProductKey get(String key);

    public void disable(String key);

}

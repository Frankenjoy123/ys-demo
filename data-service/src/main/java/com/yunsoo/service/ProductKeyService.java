package com.yunsoo.service;

import com.yunsoo.service.contract.ProductKey;

public interface ProductKeyService {

    public ProductKey get(String key);

    public void setDisabled(String key, Boolean disable);
}

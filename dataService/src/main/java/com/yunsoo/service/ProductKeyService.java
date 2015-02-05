package com.yunsoo.service;

import com.yunsoo.service.contract.ProductKeyBatchCreateRequest;
import com.yunsoo.service.contract.ProductKeyBatchCreateResponse;

public interface ProductKeyService {

    public ProductKeyBatchCreateResponse batchCreate(ProductKeyBatchCreateRequest batchCreateInput);

}

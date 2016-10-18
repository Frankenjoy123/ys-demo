package com.yunsoo.key.service;


import com.yunsoo.key.dto.ProductTrace;

import java.util.List;

/**
 * Created by yan on 10/10/2016.
 */
public interface ProductKeyTraceService {

    List<ProductTrace> getByKey(String key);

    List<ProductTrace> getProductTraceListByKeys(List<String> keys);

    void save(ProductTrace trace);

    void batchSave(List<ProductTrace> traceList);



}

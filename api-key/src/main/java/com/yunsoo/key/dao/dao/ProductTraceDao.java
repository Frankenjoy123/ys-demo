package com.yunsoo.key.dao.dao;


import com.yunsoo.key.dao.model.ProductTraceModel;

import java.util.Collection;
import java.util.List;

/**
 * Created by yan on 10/10/2016.
 */
public interface ProductTraceDao {
    ProductTraceModel getByKey(String key);

    List<ProductTraceModel> batchLoad(Collection<String> keys);

    void save(ProductTraceModel productTrace);

    void batchSave(Collection<ProductTraceModel> productTraces);
}

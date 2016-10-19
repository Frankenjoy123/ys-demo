package com.yunsoo.key.service;


import com.yunsoo.key.dao.entity.ProductTraceEntity;
import com.yunsoo.key.dto.ProductTrace;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by yan on 10/10/2016.
 */
public interface ProductKeyTraceService {

    List<ProductTrace> getByKey(String key);

    List<ProductTrace> getProductTraceListByKeys(List<String> keys);

    void save(ProductTrace trace);

    void batchSave(List<ProductTrace> traceList);

    void batchSaveInMySql(List<ProductTrace> traceList);

    ProductTrace saveInMySql(ProductTrace trace);

    List<ProductTrace> search( String status);

    int getTotalProductCount( String sourceId,String sourceType,String action,DateTime start,DateTime end);



}

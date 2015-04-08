package com.yunsoo.processor.handler;

import com.yunsoo.common.data.object.ProductKeyBatchDetailedObject;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ProductKeysObject;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.processor.message.ProductKeyBatchMassage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/2
 * Descriptions:
 */
@Component
public class ProductKeyBatchHandler {

    @Autowired
    private RestClient dataAPIClient;

    public void execute(ProductKeyBatchMassage message) {
        String batchId = message.getBatchId();
        System.out.println("Processing productkeybatch: " + batchId);
        Long batchIdL = Long.parseLong(batchId);

        ProductKeyBatchObject batch = dataAPIClient.get("productkeybatch/{id}", ProductKeyBatchObject.class, batchId);
        ProductKeysObject productKeysObject = dataAPIClient.get("productkeybatch/{id}/keys", ProductKeysObject.class, batchId);

        int quantity = productKeysObject.getQuantity();
        List<Integer> productKeyTypeIds = productKeysObject.getProductKeyTypeIds();
        List<List<String>> productKeys = productKeysObject.getProductKeys();
        ProductKeyBatchDetailedObject request = new ProductKeyBatchDetailedObject();
        request.setId(batchIdL);
        request.setProductKeyTypeIds(productKeyTypeIds);
        for (int i = 0; i < quantity; i += 1000) {
            request.setProductKeys(productKeys.subList(i, quantity < i + 1000 ? quantity : i + 1000));
        }
        if (batch.getProductBaseId() != null) {
            ProductObject product = new ProductObject();
            product.setProductBaseId(batch.getProductBaseId());
            Integer productStatusId = 1; //default activated
            product.setProductStatusId(productStatusId);
            request.setProductTemplate(product);
        }

        dataAPIClient.post("productkey/batch", request, void.class);
    }
}

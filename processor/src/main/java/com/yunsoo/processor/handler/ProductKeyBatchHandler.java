package com.yunsoo.processor.handler;

import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.processor.message.ProductKeyBatchMassage;
import com.yunsoo.service.contract.Product;
import com.yunsoo.service.contract.ProductKeyBatch;
import com.yunsoo.service.contract.ProductKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
//        ProductKeyBatch batch = productKeyBatchService.getById(batchIdL);
//        String address = batch.getProductKeysAddress();
//        ProductKeys keys = productKeyBatchService.getProductKeysByAddress(address);
//        Product productTemplate = null;
//        if (batch.getProductBaseId() != null) {
//            productTemplate = new Product();
//            productTemplate.setProductBaseId(batch.getProductBaseId());
//            productTemplate.setProductStatusId(1); //default activated
//        }
//        productKeyBatchService.batchSaveProductKey(batch, keys.getProductKeys(), productTemplate);

    }
}

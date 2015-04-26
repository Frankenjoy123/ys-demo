package com.yunsoo.processor.handler;

import com.yunsoo.common.data.object.ProductKeyBatchDetailedObject;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ProductKeysObject;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.processor.message.ProductKeyBatchMassage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Integer SUB_BATCH_LIMIT = 1000;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductKeyBatchHandler.class);

    @Autowired
    private RestClient dataAPIClient;

    public void execute(ProductKeyBatchMassage message) {
        String batchId = message.getProductKeyBatchId();
        LOGGER.info("start processing productkeybatch: [message: {}]", message.toString());

        ProductKeyBatchObject batch = dataAPIClient.get("productkeybatch/{id}", ProductKeyBatchObject.class, batchId);
        ProductKeysObject productKeysObject = dataAPIClient.get("productkeybatch/{id}/keys", ProductKeysObject.class, batchId);

        int quantity = productKeysObject.getQuantity();
        List<String> productKeyTypeCodes = productKeysObject.getProductKeyTypeCodes();
        List<List<String>> productKeys = productKeysObject.getProductKeys();

        ProductKeyBatchDetailedObject request = new ProductKeyBatchDetailedObject();
        request.setId(batchId);
        request.setProductKeyTypeCodes(productKeyTypeCodes);

        for (int i = 0; i < quantity; i += SUB_BATCH_LIMIT) {
            request.setProductKeys(productKeys.subList(i, quantity < i + SUB_BATCH_LIMIT ? quantity : i + SUB_BATCH_LIMIT));
        }

        if (batch.getProductBaseId() != null) {
            ProductObject product = new ProductObject();
            product.setProductBaseId(batch.getProductBaseId());
            String productStatusCode = "activated"; //default activated
            product.setProductStatusCode(productStatusCode);
            request.setProductTemplate(product);
        }

        dataAPIClient.post("productkey/batch", request);

        LOGGER.info("finished processing productkeybatch: [message: {}, quantity: {}]", message.toString(), quantity);
    }
}

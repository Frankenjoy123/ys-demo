package com.yunsoo.processor.handler;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.message.ProductKeyBatchMassage;
import com.yunsoo.common.data.object.ProductKeyBatchDetailedObject;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ProductKeysObject;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import com.yunsoo.processor.common.LogicalQueueName;
import com.yunsoo.processor.domain.SqsDomain;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
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
    private static final int MAX_BATCH_LIMIT = 5000;
    private static final int delaySeconds = 600;  //10min
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductKeyBatchHandler.class);

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private SqsDomain domain;


    public void execute(ProductKeyBatchMassage message) {
        String batchId = message.getProductKeyBatchId();
        String productStatusCode = message.getProductStatusCode();

        LOGGER.info("start processing productkeybatch: [message: {}]", message.toString());
        DateTime start = DateTime.now();

        ProductKeyBatchObject batch = dataAPIClient.get("productkeybatch/{id}", ProductKeyBatchObject.class, batchId);

        String productKeyBatchStatusCode = batch.getStatusCode();
        if (!LookupCodes.ProductKeyBatchStatus.CREATING.equals(productKeyBatchStatusCode)) {
            LOGGER.error("productkeybatch status is not valid [message: {}, productKeyBatchStatus: {}]",
                    message.toString(), productKeyBatchStatusCode);
            throw new RuntimeException("productkeybatch status is not valid");
        }

        ProductKeysObject productKeysObject = dataAPIClient.get("productkeybatch/{id}/keys", ProductKeysObject.class, batchId);

        int quantity = productKeysObject.getQuantity();
        List<String> productKeyTypeCodes = productKeysObject.getProductKeyTypeCodes();
        List<List<String>> productKeys = productKeysObject.getProductKeys();
        String productBaseId = batch.getProductBaseId();

        ProductKeyBatchDetailedObject request = new ProductKeyBatchDetailedObject();
        request.setId(batchId);
        request.setProductKeyTypeCodes(productKeyTypeCodes);
        if (productBaseId != null) {
            if (productStatusCode == null) {
                productStatusCode = LookupCodes.ProductStatus.CREATED;
            }
            ProductObject product = new ProductObject();
            product.setProductBaseId(productBaseId);
            product.setProductStatusCode(productStatusCode);
            request.setProductTemplate(product);
        }

        int loopRestQuantity = MAX_BATCH_LIMIT;
        int restQuantity = batch.getRestQuantity() - MAX_BATCH_LIMIT;
        for (int i = quantity - batch.getRestQuantity(); i < quantity; i += SUB_BATCH_LIMIT) {
            request.setProductKeys(productKeys.subList(i, quantity < i + SUB_BATCH_LIMIT ? quantity : i + SUB_BATCH_LIMIT));
            //batch create product key
            dataAPIClient.put("productkey/batch", request);

            loopRestQuantity -= SUB_BATCH_LIMIT;
            //only if the original restQuantity > Max_BATCH_LIMIT and there are batch items smaller than current rest quantity,
            // end the current creating and re-add the item to message queue for next round

            if( restQuantity > 0 && loopRestQuantity<=0 && exists(restQuantity)){
                loopRestQuantity = MAX_BATCH_LIMIT;
                domain.sendMessage(message, (long)delaySeconds);
                batch.setRestQuantity(restQuantity );
                dataAPIClient.patch("productkeybatch/{id}", batch, batchId);

                long seconds = (DateTime.now().getMillis() - start.getMillis()) / 1000;
                LOGGER.info("processing productkeybatch exceed max batch limit: [message: {}, quantity: {}, rest quantity: {}, seconds: {}, max limit: {}]", message.toString(), quantity, restQuantity, seconds, MAX_BATCH_LIMIT);
                return;
            }

        }

        batch.setStatusCode("available");
        batch.setRestQuantity(0);
        dataAPIClient.patch("productkeybatch/{id}", batch, batchId);

        long seconds = (DateTime.now().getMillis() - start.getMillis()) / 1000;

        LOGGER.info("finished processing productkeybatch: [message: {}, quantity: {}, seconds: {}]", message.toString(), quantity, seconds);
    }

    private boolean exists(int quantity){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("status_code_in", LookupCodes.ProductKeyBatchStatus.CREATING ).append("quantity", quantity)
                .build();
        return dataAPIClient.get("productkeybatch/exists" + query, boolean.class);
    }
}

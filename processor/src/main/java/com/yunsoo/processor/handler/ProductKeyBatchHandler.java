package com.yunsoo.processor.handler;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.message.ProductKeyBatchMassage;
import com.yunsoo.common.data.object.ProductKeyBatchDetailedObject;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ProductKeysObject;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import com.yunsoo.processor.domain.SqsDomain;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/2
 * Descriptions:
 */
@Component
public class ProductKeyBatchHandler {

    @Value("${yunsoo.sqs.handler.batch}")
    private int SUB_BATCH_LIMIT;

    @Value("${yunsoo.sqs.handler.max}")
    private int MAX_BATCH_LIMIT;

    @Value("${yunsoo.sqs.message.delayseconds}")
    private int DELAY_SECONDS;  //10min

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private SqsDomain sqsDomain;

    public void execute(ProductKeyBatchMassage message) {
        String batchId = message.getProductKeyBatchId();
        String productStatusCode = message.getProductStatusCode();

        log.info(String.format("start processing productkeybatch: [message: %s]", message.toString()));
        DateTime start = DateTime.now();

        ProductKeyBatchObject batch = dataAPIClient.get("productkeybatch/{id}", ProductKeyBatchObject.class, batchId);

        String productKeyBatchStatusCode = batch.getStatusCode();
        if (!LookupCodes.ProductKeyBatchStatus.CREATING.equals(productKeyBatchStatusCode)) {
            log.error(String.format("productkeybatch status is not valid [message: %s, productKeyBatchStatus: %s]",
                    message.toString(), productKeyBatchStatusCode));
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
                sqsDomain.sendMessage(message, (long) DELAY_SECONDS);
                batch.setRestQuantity(restQuantity );
                dataAPIClient.patch("productkeybatch/{id}", batch, batchId);

                long seconds = (DateTime.now().getMillis() - start.getMillis()) / 1000;
                log.info(String.format("processing productkeybatch exceed max batch limit: [message: %s, quantity: %d, rest quantity: %d, seconds: %d, max limit: %d]", message.toString(), quantity, restQuantity, seconds, MAX_BATCH_LIMIT));
                return;
            }

        }

        batch.setStatusCode("available");
        batch.setRestQuantity(0);
        dataAPIClient.patch("productkeybatch/{id}", batch, batchId);

        long seconds = (DateTime.now().getMillis() - start.getMillis()) / 1000;

        log.info(String.format("finished processing productkeybatch: [message: %s, quantity: %d, seconds: %d]", message.toString(), quantity, seconds));
    }

    private boolean exists(int quantity){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("status_code_in", LookupCodes.ProductKeyBatchStatus.CREATING ).append("quantity", quantity)
                .build();
        return dataAPIClient.get("productkeybatch/exists" + query, boolean.class);
    }
}

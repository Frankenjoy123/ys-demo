package com.yunsoo.processor.sqs.handler.impl;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.message.ProductKeyBatchCreateMessage;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.processor.domain.ProductKeyDomain;
import com.yunsoo.processor.sqs.MessageService;
import com.yunsoo.processor.sqs.handler.MessageHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-04-26
 * Descriptions:
 */
@Component
public class ProductKeyBatchCreateHandler implements MessageHandler<ProductKeyBatchCreateMessage> {

    private static final int BATCH_LIMIT = 1000;
    private static final int TIMEOUT_SECONDS = 600;
    private static final long DELAY_SECONDS = 60;

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private ProductKeyDomain productKeyDomain;

    @Autowired
    private MessageService messageService;

    @Override
    public void process(ProductKeyBatchCreateMessage message) {
        String productKeyBatchId = message.getProductKeyBatchId();
        String productStatusCode = !StringUtils.isEmpty(message.getProductStatusCode())
                ? message.getProductStatusCode()
                : LookupCodes.ProductStatus.CREATED;

        ProductKeyBatchObject productKeyBatchObject = productKeyDomain.getProductKeyBatchById(productKeyBatchId);
        if (productKeyBatchObject == null) {
            log.error("product key batch not found " + StringFormatter.formatMap("id", productKeyBatchId));
            throw new RuntimeException("product key batch not found");
        }

        List<List<String>> productKeys = productKeyDomain.getProductKeys(productKeyBatchObject);
        if (productKeys == null) {
            log.error("product keys not found " + StringFormatter.formatMap(
                    "orgId", productKeyBatchObject.getOrgId(),
                    "productKeyBatchId", productKeyBatchObject.getId()));
            throw new RuntimeException("product keys not found");
        }

        int quantity = productKeys.size();
        int continueOffset = message.getContinueOffset() != null ? message.getContinueOffset() : 0;

        log.info("started processing productKeyBatch " + StringFormatter.formatMap("message", message));
        DateTime startDateTime = DateTime.now();
        DateTime timeOutDateTime = startDateTime.plusSeconds(TIMEOUT_SECONDS);
        DateTime batchStartDateTime;

        for (int i = continueOffset; i < quantity; i += BATCH_LIMIT) {
            int toIndex = quantity < i + BATCH_LIMIT ? quantity : i + BATCH_LIMIT;
            List<List<String>> subList = productKeys.subList(i, toIndex);

            batchStartDateTime = DateTime.now();
            productKeyDomain.batchCreateProductKeys(productKeyBatchObject, subList, productStatusCode);

            log.info("batch saved productKeys " + StringFormatter.formatMap(
                    "count", toIndex - i,
                    "seconds", (DateTime.now().getMillis() - batchStartDateTime.getMillis()) / 1000.0));

            if (toIndex < quantity && timeOutDateTime.getMillis() < DateTime.now().getMillis()) {
                //timeout
                log.info("timeout processing productKeyBatch " + StringFormatter.formatMap(
                        "message", message,
                        "processedCount", toIndex - continueOffset,
                        "seconds", (DateTime.now().getMillis() - startDateTime.getMillis()) / 1000.0));
                message.setContinueOffset(toIndex);
                messageService.sendMessage(message, DELAY_SECONDS); //send another new message with delay seconds
                return;
            }
        }

        //finished successfully
        productKeyDomain.updateProductKeyBatchStatus(productKeyBatchId, LookupCodes.ProductKeyBatchStatus.AVAILABLE);

        log.info("finished processing productKeyBatch " + StringFormatter.formatMap(
                "message", message,
                "seconds", (DateTime.now().getMillis() - startDateTime.getMillis()) / 1000.0));
    }

    @Override
    public long getTimeout() {
        return 20 * 60 * 1000; //20
    }

}

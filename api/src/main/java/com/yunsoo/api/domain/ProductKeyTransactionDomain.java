package com.yunsoo.api.domain;

import com.yunsoo.api.dto.ProductKeyOrder;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ProductKeyTransactionObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/7
 * Descriptions:
 */
@Component
public class ProductKeyTransactionDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private ProductKeyOrderDomain productKeyOrderDomain;

    public List<ProductKeyTransactionObject> getCreatedTransactionByOrderId(String orderId) {
        String statusCode = LookupCodes.ProductKeyTransactionStatus.CREATED;

        return dataAPIClient.get("productKeyTransaction?order_id={0}&status_code={1}", new ParameterizedTypeReference<List<ProductKeyTransactionObject>>() {
        }, orderId, statusCode);
    }


    public String purchase(final ProductKeyBatchObject batch) {
        int quantity = batch.getQuantity();
        String productBaseId = batch.getProductBaseId();
        String orgId = batch.getOrgId();
        String productKeyBatchId = batch.getId();
        String accountId = batch.getCreatedAccountId();

        //get available orders
        List<ProductKeyOrder> orders = productKeyOrderDomain.getOrdersByFilter(orgId, true, null, DateTime.now(), productBaseId == null ? "*" : productBaseId, null, null);

        //sort orders by expire datetime asc
        orders.sort((o1, o2) -> {
            DateTime exp1 = o1.getExpireDateTime();
            DateTime exp2 = o2.getExpireDateTime();
            if (exp1 == null || exp2 == null) {
                return exp1 == null ? (exp2 == null ? 0 : 1) : -1;
            } else {
                return Long.compare(exp1.getMillis(), exp2.getMillis());
            }
        });

        //try purchase
        List<ProductKeyTransactionObject.Detail> details = new ArrayList<>();
        long quantityLeft = quantity;
        for (ProductKeyOrder o : orders) {
            Long remain = o.getRemain();
            if (quantityLeft > 0
                    && remain != null && remain > 0
                    && (o.getProductBaseId() == null || o.getProductBaseId().equals(productBaseId))) {
                ProductKeyTransactionObject.Detail detail = new ProductKeyTransactionObject.Detail();
                detail.setOrderId(o.getId());
                detail.setStatusCode(LookupCodes.ProductKeyTransactionStatus.CREATED);
                long decrease = quantityLeft <= remain ? quantityLeft : remain;
                detail.setQuantity(decrease);
                quantityLeft -= decrease;
                details.add(detail);
            }
        }
        if (quantityLeft > 0) {
            throw new UnprocessableEntityException("产品码可用额度不足");
        }

        ProductKeyTransactionObject request = new ProductKeyTransactionObject();
        request.setOrgId(orgId);
        request.setProductKeyBatchId(productKeyBatchId);
        request.setCreatedAccountId(accountId);
        request.setCreatedDateTime(DateTime.now());
        request.setDetails(details);

        //create transaction
        ProductKeyTransactionObject transactionObject = dataAPIClient.post("productKeyTransaction", request, ProductKeyTransactionObject.class);

        return transactionObject.getId();
    }


    public void rollback(String transactionId) {

    }

}

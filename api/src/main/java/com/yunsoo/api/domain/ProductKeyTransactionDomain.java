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
    private RestClient dataApiClient;

    @Autowired
    private ProductKeyOrderDomain productKeyOrderDomain;

    public List<ProductKeyTransactionObject> getCreatedTransactionByOrderId(String orderId) {
        String statusCode = LookupCodes.ProductKeyTransactionStatus.CREATED;
        return dataApiClient.get("productkeytransaction?order_id={0}&status_code={1}", new ParameterizedTypeReference<List<ProductKeyTransactionObject>>() {
        }, orderId, statusCode);
    }

    public List<ProductKeyTransactionObject> getCommittedTransactionByOrderId(String orderId) {
        String statusCode = LookupCodes.ProductKeyTransactionStatus.COMMITTED;
        return dataApiClient.get("productkeytransaction?order_id={0}&status_code={1}", new ParameterizedTypeReference<List<ProductKeyTransactionObject>>() {
        }, orderId, statusCode);
    }

    /**
     * get the quantity that has not been committed to the order
     *
     * @param orderId orderId
     * @return sum of quantity
     */
    public long getQuantityInTransaction(String orderId) {
        long quantity = 0L;
        List<ProductKeyTransactionObject> transactions = getCreatedTransactionByOrderId(orderId);
        if (transactions == null || transactions.size() == 0) {
            return quantity;
        }
        for (ProductKeyTransactionObject transaction : transactions) {
            for (ProductKeyTransactionObject.Detail detail : transaction.getDetails()) {
                if (detail.getQuantity() != null) {
                    quantity += detail.getQuantity();
                }
            }
        }
        return quantity;
    }

    public String purchase(final ProductKeyBatchObject batch) {
        int quantity = batch.getQuantity();
        String productBaseId = batch.getProductBaseId();
        String orgId = batch.getOrgId();
        String productKeyBatchId = batch.getId();
        String accountId = batch.getCreatedAccountId();

        //get available orders
        List<ProductKeyOrder> orders = productKeyOrderDomain.getOrdersByFilter(
                orgId,
                true,
                null,
                DateTime.now(),
                productBaseId == null ? "*" : productBaseId, null, null, null, null)
                .getContent();

        //sort orders by expire datetime asc(null last), productBaseId null last
        orders.sort((o1, o2) -> {
            DateTime exp1 = o1.getExpireDateTime();
            DateTime exp2 = o2.getExpireDateTime();
            int result;
            if (exp1 == null || exp2 == null) {
                //datetime null last
                result = exp1 == null ? (exp2 == null ? 0 : 1) : -1;
            } else {
                //datetime asc
                result = Long.compare(exp1.getMillis(), exp2.getMillis());
            }
            if (result == 0) {
                //productBaseId null last
                String productBaseId1 = o1.getProductBaseId();
                String productBaseId2 = o2.getProductBaseId();
                result = productBaseId1 == null
                        ? (productBaseId2 == null ? 0 : 1)
                        : (productBaseId2 == null ? -1 : 0);

            }
            return result;
        });

        //try purchase
        List<ProductKeyTransactionObject.Detail> details = new ArrayList<>();
        long quantityLeft = quantity;
        for (ProductKeyOrder o : orders) {
            Long remain = o.getRemain();
            remain -= getQuantityInTransaction(o.getId()); //subtract quantity in transaction which has not been committed
            if (quantityLeft > 0
                    && remain != null && remain > 0
                    && (o.getProductBaseId() == null || o.getProductBaseId().equals(productBaseId))) {
                ProductKeyTransactionObject.Detail detail = new ProductKeyTransactionObject.Detail();
                detail.setOrderId(o.getId());
                detail.setStatusCode(LookupCodes.ProductKeyTransactionStatus.CREATED);
                long spend = quantityLeft <= remain ? quantityLeft : remain;
                detail.setQuantity(spend);
                quantityLeft -= spend;
                details.add(detail);
            }
            if (quantityLeft <= 0) {
                break;
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
        ProductKeyTransactionObject transactionObject = dataApiClient.post("productkeytransaction", request, ProductKeyTransactionObject.class);

        return transactionObject.getId();
    }

    public void commit(String transactionId) {
        dataApiClient.post("productkeytransaction/{transactionId}/commit", null, ProductKeyTransactionObject.class, transactionId);
    }

    public void rollback(String transactionId) {
        dataApiClient.post("productkeytransaction/{transactionId}/rollback", null, ProductKeyTransactionObject.class, transactionId);
    }

}

package com.yunsoo.api.domain;

import com.yunsoo.api.dto.ProductKeyCredit;
import com.yunsoo.api.dto.ProductKeyOrder;
import com.yunsoo.common.data.object.ProductKeyOrderObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/6
 * Descriptions:
 */
@Component
public class ProductKeyOrderDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private ProductKeyTransactionDomain productKeyTransactionDomain;


    public ProductKeyOrder getById(String orderId) {
        ProductKeyOrderObject orderObject;
        try {
            orderObject = dataAPIClient.get("productKeyOrder/{id}", ProductKeyOrderObject.class, orderId);
        } catch (NotFoundException ex) {
            return null;
        }
        return toProductKeyOrder(orderObject);
    }

    /**
     * get orders that not expired and with active status.
     *
     * @param orgId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<ProductKeyOrder> getAvailableOrdersByOrgId(String orgId, Integer pageIndex, Integer pageSize) {
        return getOrdersByFilter(orgId, true, null, DateTime.now(), null, pageIndex, pageSize);
    }

    /**
     * @param orgId
     * @param active
     * @param remainGE
     * @param expireDateTimeGE
     * @param productBaseId    if you only want to get the orders with null productBaseId, just passing throw *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<ProductKeyOrder> getOrdersByFilter(String orgId, Boolean active, Long remainGE, DateTime expireDateTimeGE, String productBaseId, Integer pageIndex, Integer pageSize) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("active", active)
                .append("remain_ge", remainGE)
                .append("expire_datetime_ge", expireDateTimeGE)
                .append("product_base_id", productBaseId)
                .append("pageIndex", pageIndex)
                .append("pageSize", pageSize)
                .build();

        List<ProductKeyOrderObject> orderObjects = dataAPIClient.get("productKeyOrder" + query, new ParameterizedTypeReference<List<ProductKeyOrderObject>>() {
        });

        return orderObjects.stream().map(this::toProductKeyOrder).collect(Collectors.toList());
    }

    public ProductKeyOrder create(ProductKeyOrder order) {
        ProductKeyOrderObject object = toProductKeyOrderObject(order);
        ProductKeyOrderObject newObject = dataAPIClient.post("productKeyOrder", object, ProductKeyOrderObject.class);
        return toProductKeyOrder(newObject);
    }

    public List<ProductKeyCredit> getProductKeyCredits(String orgId, String productBaseId) {
        List<ProductKeyCredit> credits = new ArrayList<>();
        Map<String, ProductKeyCredit> creditMap = new HashMap<>();
        // search orders by [active = true and expire_datetime >= now], including remain is 0
        List<ProductKeyOrder> orders = getOrdersByFilter(orgId, true, null, DateTime.now(), productBaseId, null, null);

        orders.forEach(o -> {
            if (o != null) {
                String pId = o.getProductBaseId();
                ProductKeyCredit c;
                long quantityInTransaction = productKeyTransactionDomain.getQuantityInTransaction(o.getId());
                long orderRemain = o.getRemain() - quantityInTransaction;
                if (orderRemain < 0) {
                    orderRemain = 0; //ignore negative
                }
                if (creditMap.containsKey(pId)) {
                    c = creditMap.get(pId);
                } else {
                    c = new ProductKeyCredit(pId);
                    creditMap.put(pId, c);
                    credits.add(c);
                }
                c.setTotal(c.getTotal() + o.getTotal());
                c.setRemain(c.getRemain() + orderRemain);
            }
        });

        return credits;
    }


    private ProductKeyOrder toProductKeyOrder(ProductKeyOrderObject object) {
        if (object == null) {
            return null;
        }
        ProductKeyOrder productKeyOrder = new ProductKeyOrder();
        productKeyOrder.setId(object.getId());
        productKeyOrder.setOrgId(object.getOrgId());
        productKeyOrder.setTotal(object.getTotal());
        productKeyOrder.setRemain(object.getRemain());
        productKeyOrder.setActive(object.getActive());
        productKeyOrder.setProductBaseId(object.getProductBaseId());
        productKeyOrder.setDescription(object.getDescription());
        productKeyOrder.setCreatedAccountId(object.getCreatedAccountId());
        productKeyOrder.setCreatedDateTime(object.getCreatedDateTime());
        productKeyOrder.setExpireDateTime(object.getExpireDateTime());
        return productKeyOrder;
    }

    private ProductKeyOrderObject toProductKeyOrderObject(ProductKeyOrder order) {
        if (order == null) {
            return null;
        }
        ProductKeyOrderObject object = new ProductKeyOrderObject();
        object.setId(order.getId());
        object.setOrgId(order.getOrgId());
        object.setTotal(order.getTotal());
        object.setRemain(order.getRemain());
        object.setActive(order.getActive());
        object.setProductBaseId(order.getProductBaseId());
        object.setDescription(order.getDescription());
        object.setCreatedAccountId(order.getCreatedAccountId());
        object.setCreatedDateTime(order.getCreatedDateTime());
        object.setExpireDateTime(order.getExpireDateTime());
        return object;
    }
}

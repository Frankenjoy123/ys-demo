package com.yunsoo.api.domain;

import com.yunsoo.api.dto.ProductKeyCredit;
import com.yunsoo.api.dto.ProductKeyOrder;
import com.yunsoo.common.data.object.ProductKeyOrderObject;
import com.yunsoo.common.util.DateTimeUtils;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
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


    public ProductKeyOrder getById(String orderId) {
        ProductKeyOrderObject orderObject;
        try {
            orderObject = dataAPIClient.get("productKeyOrder/{id}", ProductKeyOrderObject.class, orderId);
        } catch (NotFoundException ex) {
            return null;
        }
        return toProductKeyOrder(orderObject);
    }

    public List<ProductKeyOrder> getAvailableOrdersByOrgId(String orgId, Integer pageIndex, Integer pageSize) {
        return getOrdersByFilter(orgId, true, null, DateTime.now(), null, pageIndex, pageSize);
    }

    public List<ProductKeyOrder> getOrdersByFilter(String orgId, Boolean active, Long remainGE, DateTime expireDateTimeGE, String productBaseId, Integer pageIndex, Integer pageSize) {
        List<ProductKeyOrderObject> orderObjects = dataAPIClient.get("productKeyOrder?org_id={orgId}&active={active}&remain_ge={remainGE}&expire_datetime_ge={now}&product_base_id={productBaseId}pageIndex={pageIndex}&pageSize={pageSize}", new ParameterizedTypeReference<List<ProductKeyOrderObject>>() {
        }, orgId, active, remainGE, DateTimeUtils.toUTCString(expireDateTimeGE), productBaseId, pageIndex, pageSize);

        return orderObjects.stream().map(this::toProductKeyOrder).collect(Collectors.toList());
    }

    private List<ProductKeyCredit> getProductKeyCredits(String orgId, String productBaseId) {
        List<ProductKeyCredit> credits = new ArrayList<>();
        Map<String, ProductKeyCredit> creditMap = new HashMap<>();
        List<ProductKeyOrder> orders = getOrdersByFilter(orgId, true, 1L, DateTime.now(), productBaseId, null, null);
        orders.forEach(o -> {
            if (o != null) {
                String pId = o.getProductBaseId();
                ProductKeyCredit c;
                if (creditMap.containsKey(pId)) {
                    c = creditMap.get(pId);
                } else {
                    c = new ProductKeyCredit(pId);
                    creditMap.put(pId, c);
                    credits.add(c);
                }
                c.setTotalCredit(c.getTotalCredit() + o.getTotal());
                c.setRemainCredit(c.getRemainCredit() + o.getRemain());
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
}

package com.yunsoo.api.domain;

import com.yunsoo.api.dto.ProductKeyOrder;
import com.yunsoo.common.data.object.ProductKeyOrderObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;
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
        String query = new QueryStringBuilder()
                .append("org_id", orgId)
                .append("active", active)
                .append("remain_ge", remainGE)
                .append("expire_datetime_ge", expireDateTimeGE)
                .append("product_base_id", productBaseId)
                .append("pageIndex", pageIndex)
                .append("pageSize", pageSize)
                .build();

        List<ProductKeyOrderObject> orderObjects = dataAPIClient.get("productKeyOrder?{query}", new ParameterizedTypeReference<List<ProductKeyOrderObject>>() {
        }, query);

        return orderObjects.stream().map(this::toProductKeyOrder).collect(Collectors.toList());
    }

    public ProductKeyOrder create(ProductKeyOrder order) {
        return null;
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

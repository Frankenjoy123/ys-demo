package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductKeyOrderDomain;
import com.yunsoo.api.dto.ProductKeyOrder;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/6
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/productKeyOrder")
public class ProductKeyOrderController {

    @Autowired
    private ProductKeyOrderDomain productKeyOrderDomain;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductKeyOrder getById(@PathVariable(value = "id") String id) {
        ProductKeyOrder productKeyOrder = productKeyOrderDomain.getById(id);
        if (productKeyOrder == null) {
            throw new NotFoundException("ProductKeyOrder not found by [id: " + id + "]");
        }
        return productKeyOrder;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductKeyOrder> getByFilter(@RequestParam(value = "org_id") String orgId,
                                             @RequestParam(value = "available", required = false) Boolean available,
                                             @RequestParam(value = "active", required = false) Boolean active,
                                             @RequestParam(value = "remain_ge", required = false) Long remainGE,
                                             @RequestParam(value = "expire_datetime_ge", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime expireDateTimeGE,
                                             @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                             @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                             @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return available != null && available
                ? productKeyOrderDomain.getAvailableOrdersByOrgId(orgId, pageIndex, pageSize)
                : productKeyOrderDomain.getOrdersByFilter(orgId, active, remainGE, expireDateTimeGE, productBaseId, pageIndex, pageSize);
    }


}

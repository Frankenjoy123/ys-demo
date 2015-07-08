package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductKeyOrderDomain;
import com.yunsoo.api.dto.ProductKeyOrder;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductKeyOrder getById(@PathVariable(value = "id") String id) {
        ProductKeyOrder productKeyOrder = productKeyOrderDomain.getById(id);
        if (productKeyOrder == null) {
            throw new NotFoundException("ProductKeyOrder not found by [id: " + id + "]");
        }
        return productKeyOrder;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'filterByOrg', 'productkeyorder:read')")
    public List<ProductKeyOrder> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                             @RequestParam(value = "available", required = false) Boolean available,
                                             @RequestParam(value = "active", required = false) Boolean active,
                                             @RequestParam(value = "remain_ge", required = false) Long remainGE,
                                             @RequestParam(value = "expire_datetime_ge", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime expireDateTimeGE,
                                             @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                             Pageable pageable,
                                             HttpServletResponse response) {
        if (orgId == null) {
            orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }

        Page<ProductKeyOrder> orderPage = available != null && available
                ? productKeyOrderDomain.getAvailableOrdersByOrgId(orgId, pageable)
                : productKeyOrderDomain.getOrdersByFilter(orgId, active, remainGE, expireDateTimeGE, productBaseId, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", orderPage.toContentRange());
        }

        return orderPage.getContent();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#order, 'productkeyorder:create')")
    public ProductKeyOrder create(@Valid @RequestBody ProductKeyOrder order) {
        String orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        String accountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        order.setId(null);
        if (order.getOrgId() == null || order.getOrgId().trim().isEmpty()) {
            order.setOrgId(orgId);
        }
        order.setCreatedAccountId(accountId);
        order.setActive(true);
        order.setCreatedDateTime(DateTime.now());
        return productKeyOrderDomain.create(order);
    }

}

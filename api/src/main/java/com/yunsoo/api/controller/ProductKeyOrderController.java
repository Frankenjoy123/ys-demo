package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductBaseDomain;
import com.yunsoo.api.domain.ProductKeyOrderDomain;
import com.yunsoo.api.domain.ProductKeyTransactionDomain;
import com.yunsoo.api.dto.ProductBase;
import com.yunsoo.api.dto.ProductKeyOrder;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.ProductKeyTransactionObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ProductBaseDomain productBaseDomain;

    @Autowired
    private ProductKeyTransactionDomain transactionDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductKeyOrder getById(@PathVariable(value = "id") String id) {
        ProductKeyOrder productKeyOrder = productKeyOrderDomain.getById(id);
        if (productKeyOrder == null) {
            throw new NotFoundException("ProductKeyOrder not found by [id: " + id + "]");
        }
        productKeyOrder.setTransactionList(transactionDomain.getCreatedTransactionByOrderId(id));
        if(StringUtils.hasText(productKeyOrder.getProductBaseId()))
            productKeyOrder.setProductBase(new ProductBase(productBaseDomain.getProductBaseById(productKeyOrder.getProductBaseId())));

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

        List<ProductKeyOrder> list = orderPage.getContent();
        if(productBaseId == null){
            Map<String, ProductBase> productBaseMap = new HashMap<>();
            list.forEach(item->{
                if(productBaseMap.containsKey(item.getProductBaseId()))
                    item.setProductBase(productBaseMap.get(item.getProductBaseId()));
                else{
                    ProductBase productBase = new ProductBase(productBaseDomain.getProductBaseById(item.getProductBaseId()));
                    item.setProductBase(productBase);
                    productBaseMap.put(item.getProductBaseId(), productBase);
                }
            });
        }
        return list;
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
        if (order.getProductBaseId() != null && order.getProductBaseId().length() == 0) {
            order.setProductBaseId(null);
        }
        order.setCreatedAccountId(accountId);
        order.setActive(true);
        order.setCreatedDateTime(DateTime.now());
        return productKeyOrderDomain.create(order);
    }

    @RequestMapping(value = "{id}/activate", method = RequestMethod.PATCH)
    public void activate(@PathVariable("id") String id){
        ProductKeyOrder productKeyOrder = new ProductKeyOrder();
        productKeyOrder.setId(id);
        productKeyOrder.setActive(true);
        productKeyOrderDomain.patchUpdate(productKeyOrder);

    }

    @RequestMapping(value = "{id}/deactivate", method = RequestMethod.PATCH)
    public void deactivate(@PathVariable("id") String id){
        ProductKeyOrder productKeyOrder = new ProductKeyOrder();
        productKeyOrder.setId(id);
        productKeyOrder.setActive(false);
        productKeyOrderDomain.patchUpdate(productKeyOrder);

    }

}

package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductBaseDomain;
import com.yunsoo.api.domain.ProductKeyDomain;
import com.yunsoo.api.domain.ProductKeyOrderDomain;
import com.yunsoo.api.domain.ProductKeyTransactionDomain;
import com.yunsoo.api.dto.ProductBase;
import com.yunsoo.api.dto.ProductKeyBatch;
import com.yunsoo.api.dto.ProductKeyOrder;
import com.yunsoo.api.dto.ProductKeyTransaction;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private ProductKeyDomain productKeyDomain;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'product_key_order:read')")
    public ProductKeyOrder getById(@PathVariable(value = "id") String id) {
        ProductKeyOrder productKeyOrder = productKeyOrderDomain.getById(id);
        if (productKeyOrder == null) {
            throw new NotFoundException("ProductKeyOrder not found by [id: " + id + "]");
        }
        List<ProductKeyTransaction> transactionList = transactionDomain.getCommittedTransactionByOrderId(id).stream().map(ProductKeyTransaction::new).collect(Collectors.toList());
        productKeyOrder.setTransactionList(transactionList);
        if (StringUtils.hasText(productKeyOrder.getProductBaseId()))
            productKeyOrder.setProductBase(new ProductBase(productBaseDomain.getProductBaseById(productKeyOrder.getProductBaseId())));

        HashMap<String, ProductKeyBatch> productKeyTransactionHashMap = new HashMap<>();
        HashMap<String, ProductBase> productBaseHashMap = new HashMap<>();
        for (ProductKeyTransaction transaction : transactionList) {
            if (productKeyTransactionHashMap.containsKey(transaction.getProductKeyBatchId()))
                transaction.setKeyBatch(productKeyTransactionHashMap.get(transaction.getProductKeyBatchId()));
            else {
                ProductKeyBatch batch = new ProductKeyBatch(productKeyDomain.getPkBatchById(transaction.getProductKeyBatchId()));
                transaction.setKeyBatch(batch);
                productKeyTransactionHashMap.put(transaction.getProductKeyBatchId(), batch);
            }

            if (StringUtils.hasText(productKeyOrder.getProductBaseId()))
                transaction.setProductBase(productKeyOrder.getProductBase());
            else {
                String productBaseId = transaction.getKeyBatch().getProductBaseId();
                if (productBaseHashMap.containsKey(productBaseId))
                    transaction.setProductBase(productBaseHashMap.get(productBaseId));
                else {
                    ProductBase base = new ProductBase(productBaseDomain.getProductBaseById(productBaseId));
                    transaction.setProductBase(base);
                    productBaseHashMap.put(productBaseId, base);
                }
            }


        }

        return productKeyOrder;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'product_key_order:read')")
    public List<ProductKeyOrder> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                             @RequestParam(value = "available", required = false) Boolean available,
                                             @RequestParam(value = "active", required = false) Boolean active,
                                             @RequestParam(value = "remain_ge", required = false) Long remainGE,
                                             @RequestParam(value = "expire_datetime_ge", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime expireDateTimeGE,
                                             @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                             Pageable pageable,
                                             HttpServletResponse response) {
        orgId = AuthUtils.fixOrgId(orgId);

        Page<ProductKeyOrder> orderPage = available != null && available
                ? productKeyOrderDomain.getAvailableOrdersByOrgId(orgId, pageable)
                : productKeyOrderDomain.getOrdersByFilter(orgId, active, remainGE, expireDateTimeGE, productBaseId, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", orderPage.toContentRange());
        }

        List<ProductKeyOrder> list = orderPage.getContent();
        if (productBaseId == null) {
            Map<String, ProductBase> productBaseMap = new HashMap<>();
            list.forEach(item -> {
                if (productBaseMap.containsKey(item.getProductBaseId()))
                    item.setProductBase(productBaseMap.get(item.getProductBaseId()));
                else {
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
    @PreAuthorize("hasPermission(#order, 'product_key_order:create')")
    public ProductKeyOrder create(@Valid @RequestBody ProductKeyOrder order) {
        order.setId(null);
        order.setOrgId(AuthUtils.fixOrgId(order.getOrgId()));
        if (order.getProductBaseId() != null && order.getProductBaseId().length() == 0) {
            order.setProductBaseId(null);
        }
        order.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        order.setActive(true);
        order.setCreatedDateTime(DateTime.now());
        return productKeyOrderDomain.create(order);
    }

    @RequestMapping(value = "{id}/activate", method = RequestMethod.PATCH)
    public void activate(@PathVariable("id") String id) {
        ProductKeyOrder productKeyOrder = new ProductKeyOrder();
        productKeyOrder.setId(id);
        productKeyOrder.setActive(true);
        productKeyOrderDomain.patchUpdate(productKeyOrder);

    }

    @RequestMapping(value = "{id}/deactivate", method = RequestMethod.PATCH)
    public void deactivate(@PathVariable("id") String id) {
        ProductKeyOrder productKeyOrder = new ProductKeyOrder();
        productKeyOrder.setId(id);
        productKeyOrder.setActive(false);
        productKeyOrderDomain.patchUpdate(productKeyOrder);

    }

    @RequestMapping(value = "/count/total", method = RequestMethod.GET)
    public long countTotal(@RequestParam("org_ids") List<String> orgIds) {
        return productKeyOrderDomain.count(orgIds, true);
    }

    @RequestMapping(value = "/count/remain", method = RequestMethod.GET)
    public long countRemain(@RequestParam("org_ids") List<String> orgIds) {
        return productKeyOrderDomain.count(orgIds, false);
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public List<ProductKeyOrder> statistics(@RequestParam("org_ids") List<String> orgIds, Pageable pageable) {
        return productKeyOrderDomain.statistics(orgIds, pageable);
    }


}

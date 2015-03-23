package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductDomain;
import com.yunsoo.api.domain.ProductKeyDomain;
import com.yunsoo.api.dto.ProductKeyBatch;
import com.yunsoo.api.dto.ProductKeyBatchRequest;
import com.yunsoo.api.dto.basic.ProductBase;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ProductKeyBatchRequestObject;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/19
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/productkeybatch")
public class ProductKeyBatchController {

    @Autowired
    private ProductDomain productDomain;

    @Autowired
    private ProductKeyDomain productKeyDomain;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductKeyBatch getById(@PathVariable(value = "id") String idStr) {
        int organizationId = 1;
        long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException ex) {
            throw new BadRequestException("invalid id");
        }
        ProductKeyBatch batch = productKeyDomain.getProductKeyBatchById(id);
        if (batch == null) {
            throw new NotFoundException("product batch");
        }
        if (batch.getOrganizationId() != organizationId) {
            throw new ForbiddenException();
        }
        return batch;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductKeyBatch> getByFilter(@RequestParam(value = "productBaseId", required = false) Long productBaseId,
                                             @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                             @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int organizationId = 1;
        return productKeyDomain.getAllProductKeyBatchByOrgId(organizationId, productBaseId);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ProductKeyBatch create(@Valid @RequestBody ProductKeyBatchRequest request) {
        int quantity = request.getQuantity();
        Long productBaseId = request.getProductBaseId();
        List<String> productKeyTypeCodes = request.getProductKeyTypeCodes();
        List<Integer> productKeyTypeIds = null;
        if (productKeyTypeCodes != null && !productKeyTypeCodes.isEmpty()) {
            try {
                productKeyTypeIds = productKeyDomain.changeProductKeyTypeCodeToId(productKeyTypeCodes);
            } catch (IllegalArgumentException ex) {
                throw new BadRequestException("productKeyTypeCodes invalid");
            }
        }
        int statusId = 0;
        int organizationId = 1;
        int clientId = 1;
        Long accountId = 1L;
        DateTime createdDateTime = DateTime.now();

        ProductKeyBatchRequestObject requestObject = new ProductKeyBatchRequestObject();
        ProductKeyBatchObject batchObj = new ProductKeyBatchObject();
        if (productBaseId != null && productBaseId > 0) {
            //create corresponding product according to the productBaseId
            ProductBase productBase = productDomain.getProductBase(productBaseId);
            if (productBase == null) {
                throw new BadRequestException("product base id invalid");
            }
            if (productKeyTypeIds == null) {
                productKeyTypeIds = productBase.getProductKeyTypeIds();
            }
            int productStatusId = 0;
            ProductObject productObj = new ProductObject();
            productObj.setProductBaseId(productBaseId);
            productObj.setProductStatusId(productStatusId);
            //productObj.setManufacturingDateTime(null);
            requestObject.setProductTemplate(productObj);
        }
        batchObj.setQuantity(quantity);
        batchObj.setStatusId(statusId);
        batchObj.setOrganizationId(organizationId);
        batchObj.setProductBaseId(productBaseId);
        batchObj.setCreatedClientId(clientId);
        batchObj.setCreatedAccountId(accountId);
        batchObj.setCreatedDateTime(createdDateTime);
        batchObj.setProductKeyTypeIds(productKeyTypeIds);
        requestObject.setProductKeyBatch(batchObj);

        return productKeyDomain.createProductKeyBatch(requestObject);
    }

}

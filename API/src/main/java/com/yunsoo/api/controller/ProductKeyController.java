package com.yunsoo.api.controller;

import com.yunsoo.api.dto.ProductKey;
import com.yunsoo.api.dto.ProductKeyBatch;
import com.yunsoo.api.dto.ProductKeyBatchRequest;
import com.yunsoo.api.dto.basic.Product;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ProductKeyBatchRequestObject;
import com.yunsoo.common.data.object.ProductKeyObject;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/9
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/productkey")
public class ProductKeyController {

    private RestClient dataAPIClient;

    @Autowired
    ProductKeyController(RestClient dataAPIClient) {
        this.dataAPIClient = dataAPIClient;
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public ProductKey get(@PathVariable(value = "key") String key) {
        if (StringUtils.isEmpty(key)) {
            throw new BadRequestException("please provide a valid product key");
        }
        ProductKeyObject tProductKey = dataAPIClient.get("productkey/{key}", ProductKeyObject.class, key);
        if (tProductKey == null) {
            throw new NotFoundException("product key");
        }
        ProductKey productKey = new ProductKey();
        productKey.setProductKey(tProductKey.getProductKey());
        productKey.setProductKeyTypeId(tProductKey.getProductKeyTypeId());
        productKey.setProductKeyDisabled(tProductKey.isProductKeyDisabled());
        productKey.setPrimary(tProductKey.isPrimary());
        productKey.setProductKeyBatchId(tProductKey.getProductKeyBatchId());
        productKey.setPrimaryProductKey(tProductKey.getPrimaryProductKey());
        productKey.setProductKeySet(tProductKey.getProductKeySet());
        productKey.setCreatedDateTime(tProductKey.getCreatedDateTime());
        return productKey;
    }

    @RequestMapping(value = "{key}/disable", method = RequestMethod.PUT)
    public void disableKey(@PathVariable(value = "key") String key) {
        if (StringUtils.isEmpty(key)) {
            throw new BadRequestException("please provide a valid product key");
        }
        dataAPIClient.put("productkey/{key}/disable", null, key);
    }


    //batch

    @RequestMapping(value = "batch/{id}", method = RequestMethod.GET)
    public ProductKeyBatch getBatchById(@PathVariable(value = "id") String idStr) {
        int idInt;
        try {
            idInt = Integer.parseInt(idStr);
        } catch (NumberFormatException ex) {
            throw new BadRequestException("invalid id");
        }
        ProductKeyBatchObject batch = dataAPIClient.get("productkey/batch/{id}", ProductKeyBatchObject.class, idInt);
        if (batch == null) {
            throw new NotFoundException("product batch");
        }
        ProductKeyBatch batchDto = new ProductKeyBatch();
        batchDto.setId(batch.getId());
        batchDto.setQuantity(batch.getQuantity());
        batchDto.setStatusId(batch.getStatusId());
        batchDto.setOrganizationId(batch.getOrganizationId());
        batchDto.setCreatedClientId(batch.getCreatedClientId());
        batchDto.setCreatedAccountId(batch.getCreatedAccountId());
        batchDto.setCreatedDateTime(batch.getCreatedDateTime());
        batchDto.setProductKeyTypeIds(batch.getProductKeyTypeIds());
        return batchDto;
    }

    @RequestMapping(value = "batch/test", method = RequestMethod.POST)
    public int batchCreateProductKeysTest(@Valid @RequestBody ProductKeyBatchRequest request) {
        return request.getQuantity();
    }

    @RequestMapping(value = "batch/create", method = RequestMethod.POST)
    public ProductKeyBatch batchCreateProductKeys(@RequestBody ProductKeyBatchRequest request) {
        int quantity = request.getQuantity();
        List<Integer> productKeyTypeIds = null;//request.getProductKeyTypeIds();
        Integer productBaseId = request.getProductBaseId();

        int statusId = 0;
        int organizationId = 1;
        int clientId = 1;
        int accountId = 1;
        DateTime createdDateTime = DateTime.now();

        ProductKeyBatchRequestObject requestObject = new ProductKeyBatchRequestObject();
        ProductKeyBatchObject batchObj = new ProductKeyBatchObject();
        batchObj.setQuantity(quantity);
        batchObj.setStatusId(statusId);
        batchObj.setOrganizationId(organizationId);
        batchObj.setCreatedClientId(clientId);
        batchObj.setCreatedAccountId(accountId);
        batchObj.setCreatedDateTime(createdDateTime);
        batchObj.setProductKeyTypeIds(productKeyTypeIds);
        requestObject.setProductKeyBatch(batchObj);
        if (productBaseId != null && productBaseId > 0) {
            int productStatusId = 0;
            ProductObject productObj = new ProductObject();
            productObj.setProductBaseId(productBaseId);
            productObj.setProductStatusId(productStatusId);
            //productObj.setManufacturingDateTime(null);
            requestObject.setProductTemplate(productObj);
        }
        ProductKeyBatchObject newBatchObj = dataAPIClient.post(
                "productkey/batch/create",
                requestObject,
                ProductKeyBatchObject.class);

        ProductKeyBatch newBatch = new ProductKeyBatch();
        newBatch.setId(newBatchObj.getId());
        newBatch.setQuantity(newBatchObj.getQuantity());
        newBatch.setStatusId(newBatchObj.getStatusId());
        newBatch.setOrganizationId(newBatchObj.getOrganizationId());
        newBatch.setCreatedClientId(newBatchObj.getCreatedClientId());
        newBatch.setCreatedAccountId(newBatchObj.getCreatedAccountId());
        newBatch.setCreatedDateTime(newBatchObj.getCreatedDateTime());
        newBatch.setProductKeyTypeIds(newBatchObj.getProductKeyTypeIds());

        return newBatch;
    }

}

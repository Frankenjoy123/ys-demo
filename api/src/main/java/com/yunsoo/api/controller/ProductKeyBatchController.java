package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductKeyDomain;
import com.yunsoo.api.dto.ProductKeyBatch;
import com.yunsoo.api.dto.ProductKeyBatchRequest;
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
    private RestClient dataAPIClient;

    @Autowired
    private ProductKeyDomain productKeyDomain;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductKeyBatch> getAllForCurrentOrg() {
        int organizationId = 1;
        return productKeyDomain.getAllProductKeyBatchByOrgId(organizationId);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductKeyBatch getBatchById(@PathVariable(value = "id") String idStr) {
        int organizationId = 1;
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
        if (batch.getOrganizationId() != organizationId) {
            throw new ForbiddenException();
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

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ProductKeyBatch batchCreateProductKeys(@Valid @RequestBody ProductKeyBatchRequest request) {
        int quantity = request.getQuantity();
        List<String> productKeyTypeCodes = request.getProductKeyTypeCodes();
        Integer productBaseId = request.getProductBaseId();
        List<Integer> productKeyTypeIds;
        try {
            productKeyTypeIds = productKeyDomain.changeProductKeyTypeCodeToId(productKeyTypeCodes);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("productKeyTypeCodes invalid");
        }
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
            //create corresponding product according to the productBaseId
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

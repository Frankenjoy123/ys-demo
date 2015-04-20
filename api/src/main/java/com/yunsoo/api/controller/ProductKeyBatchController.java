package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductDomain;
import com.yunsoo.api.domain.ProductKeyDomain;
import com.yunsoo.api.dto.ProductKeyBatch;
import com.yunsoo.api.dto.ProductKeyBatchRequest;
import com.yunsoo.api.dto.basic.ProductBase;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
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
    public ProductKeyBatch getById(@PathVariable(value = "id") String id) {
        String orgId = "2k0r1l55i2rs5544wz5";
        ProductKeyBatch batch = productKeyDomain.getProductKeyBatchById(id);
        if (batch == null) {
            throw new NotFoundException("product batch");
        }
        if (!batch.getOrgId().equals(orgId)) { //todo: replace with hasPermission
            throw new ForbiddenException();
        }
        return batch;
    }

    @RequestMapping(value = "{id}/keys", method = RequestMethod.GET)
    public ResponseEntity<?> getKeysById(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd+yunsoo.pks"))
                .header("Content-Disposition", "attachment; filename=\"product_key_batch_" + id + ".pks\"")
                .body(new InputStreamResource(new ByteArrayInputStream(productKeyDomain.getProductKeysByBatchId(id))));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductKeyBatch> getByFilter(@RequestParam(value = "productBaseId", required = false) String productBaseId,
                                             @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                             @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        String orgId = "2k0r1l55i2rs5544wz5";
        return productKeyDomain.getAllProductKeyBatchesByOrgId(orgId, productBaseId);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ProductKeyBatch create(@Valid @RequestBody ProductKeyBatchRequest request) {
        int quantity = request.getQuantity();
        String productBaseId = request.getProductBaseId();
        List<String> productKeyTypeCodes = request.getProductKeyTypeCodes();

        String statusCode = "new";
        String orgId = "2k0r1l55i2rs5544wz5";
        String clientId = "1";
        String accountId = "2k0rahgcybh0l5uxtep";
        DateTime createdDateTime = DateTime.now();

        ProductKeyBatchObject batchObj = new ProductKeyBatchObject();
        if (productBaseId != null) {
            //create corresponding product according to the productBaseId
            ProductBase productBase = productDomain.getProductBaseById(productBaseId);
            if (productBase == null) {
                throw new BadRequestException("productBaseId invalid");
            }
            if (productKeyTypeCodes == null) {
                productKeyTypeCodes = productBase.getProductKeyTypeCodes();
            }
        }

        if (!productKeyDomain.validateProductKeyTypeCodes(productKeyTypeCodes)) {
            throw new BadRequestException("productKeyTypeCodes invalid");
        }

        batchObj.setQuantity(quantity);
        batchObj.setStatusCode(statusCode);
        batchObj.setProductBaseId(productBaseId);
        batchObj.setProductKeyTypeCodes(productKeyTypeCodes);
        batchObj.setOrgId(orgId);
        batchObj.setCreatedClientId(clientId);
        batchObj.setCreatedAccountId(accountId);
        batchObj.setCreatedDateTime(createdDateTime);

        return productKeyDomain.createProductKeyBatch(batchObj);
    }

}

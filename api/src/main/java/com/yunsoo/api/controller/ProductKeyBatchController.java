package com.yunsoo.api.controller;

import com.yunsoo.api.Constants;
import com.yunsoo.api.domain.PermissionDomain;
import com.yunsoo.api.domain.ProductDomain;
import com.yunsoo.api.domain.ProductKeyDomain;
import com.yunsoo.api.dto.ProductKeyBatch;
import com.yunsoo.api.dto.ProductKeyBatchRequest;
import com.yunsoo.api.dto.basic.ProductBase;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductKeyBatchController.class);

    @Autowired
    private ProductDomain productDomain;

    @Autowired
    private ProductKeyDomain productKeyDomain;

    @Autowired
    private PermissionDomain permissionDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductKeyBatch getById(@PathVariable(value = "id") String id) {
        String accountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        ProductKeyBatch batch = productKeyDomain.getProductKeyBatchById(id);
        if (batch == null) {
            throw new NotFoundException("product batch");
        }
        if (!permissionDomain.hasPermission(accountId, new TPermission(batch.getOrgId(), "productkey", "read"))) {
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
    public List<ProductKeyBatch> getByFilter(@RequestParam(value = "product_base_id", required = false) String productBaseId,
                                             @RequestParam(value = "page_index", required = false) Integer pageIndex,
                                             @RequestParam(value = "page_size", required = false) Integer pageSize,
                                             @RequestParam(value = "order_by", required = false) String orderBy) {
        String orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();


        return productKeyDomain.getAllProductKeyBatchesByOrgId(orgId, productBaseId);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ProductKeyBatch create(
            @RequestHeader(value = Constants.HttpHeaderName.APP_ID, required = false) String appId,
            @Valid @RequestBody ProductKeyBatchRequest request) {
        int quantity = request.getQuantity();
        String productBaseId = request.getProductBaseId();
        List<String> productKeyTypeCodes = request.getProductKeyTypeCodes();

        String accountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        String orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        if (!permissionDomain.hasPermission(accountId, new TPermission(orgId, "productkey", "create"))) {
            throw new ForbiddenException();
        }
        appId = (appId == null) ? "unknown" : appId;
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
        batchObj.setProductBaseId(productBaseId);
        batchObj.setProductKeyTypeCodes(productKeyTypeCodes);
        batchObj.setOrgId(orgId);
        batchObj.setCreatedAppId(appId);
        batchObj.setCreatedAccountId(accountId);
        batchObj.setCreatedDateTime(createdDateTime);

        LOGGER.info("ProductKeyBatch creating started [quantity: {}]", batchObj.getQuantity());
        ProductKeyBatch newBatch = productKeyDomain.createProductKeyBatch(batchObj);
        LOGGER.info("ProductKeyBatch created [id: {}, quantity: {}]", newBatch.getId(), newBatch.getQuantity());

        return newBatch;
    }

}

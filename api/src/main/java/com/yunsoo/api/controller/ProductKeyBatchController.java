package com.yunsoo.api.controller;

import com.yunsoo.api.Constants;
import com.yunsoo.api.domain.AccountPermissionDomain;
import com.yunsoo.api.domain.ProductBaseDomain;
import com.yunsoo.api.domain.ProductKeyDomain;
import com.yunsoo.api.dto.ProductBase;
import com.yunsoo.api.dto.ProductKeyBatch;
import com.yunsoo.api.dto.ProductKeyBatchRequest;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
    private ProductBaseDomain productBaseDomain;

    @Autowired
    private ProductKeyDomain productKeyDomain;

    @Autowired
    private AccountPermissionDomain accountPermissionDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductKeyBatch getById(@PathVariable(value = "id") String id) {
        String accountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        ProductKeyBatch batch = productKeyDomain.getProductKeyBatchById(id);
        if (batch == null) {
            throw new NotFoundException("product batch");
        }
        if (!accountPermissionDomain.hasPermission(accountId, new TPermission(batch.getOrgId(), "productkey", "read"))) {
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
    public List<ProductKeyBatch> getByFilterPaged(@RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                  @PageableDefault(page = 0, size = 20)
                                                  @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                                  Pageable pageable,
                                                  HttpServletResponse response) {
        String orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();

        Page<List<ProductKeyBatch>> page = productKeyDomain.getProductKeyBatchesByFilterPaged(orgId, productBaseId, pageable);
        response.setHeader("Content-Range", "pages " + page.getPage() + "/" + page.getTotal());
        return page.getContent();
    }

    @RequestMapping(value = "sum/quantity", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'filterByOrg', 'productkeybatch:read')")
    public Long sumQuantity(
            @RequestParam(value = "org_id", required = false) String orgId,
            @RequestParam(value = "product_base_id", required = false) String productBaseId) {
        if (StringUtils.isEmpty(orgId)) {
            orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }
        return productKeyDomain.sumQuantity(orgId, productBaseId);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductKeyBatch create(
            @RequestHeader(value = Constants.HttpHeaderName.APP_ID, required = false) String appId,
            @Valid @RequestBody ProductKeyBatchRequest request) {
        int quantity = request.getQuantity();
        String productBaseId = request.getProductBaseId();
        List<String> productKeyTypeCodes = request.getProductKeyTypeCodes();

        String accountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        String orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        if (!accountPermissionDomain.hasPermission(accountId, new TPermission(orgId, "productkey", "create"))) {
            throw new ForbiddenException();
        }
        appId = (appId == null) ? "unknown" : appId;
        DateTime createdDateTime = DateTime.now();

        ProductKeyBatchObject batchObj = new ProductKeyBatchObject();
        if (productBaseId != null) {
            //create corresponding product according to the productBaseId
            ProductBase productBase = productBaseDomain.getProductBaseById(productBaseId);
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

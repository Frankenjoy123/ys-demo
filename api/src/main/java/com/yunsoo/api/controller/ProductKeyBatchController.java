package com.yunsoo.api.controller;

import com.yunsoo.api.Constants;
import com.yunsoo.api.domain.AccountPermissionDomain;
import com.yunsoo.api.domain.FileDomain;
import com.yunsoo.api.domain.ProductBaseDomain;
import com.yunsoo.api.domain.ProductKeyDomain;
import com.yunsoo.api.dto.ProductBase;
import com.yunsoo.api.dto.ProductBatchCollection;
import com.yunsoo.api.dto.ProductKeyBatch;
import com.yunsoo.api.dto.ProductKeyBatchRequest;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
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
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/3/19
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/productkeybatch")
public class ProductKeyBatchController {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private ProductBaseDomain productBaseDomain;

    @Autowired
    private ProductKeyDomain productKeyDomain;

    @Autowired
    private AccountPermissionDomain accountPermissionDomain;

    @Autowired
    private FileDomain fileDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductKeyBatch getById(@PathVariable(value = "id") String id) {
        String accountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        ProductKeyBatch batch = productKeyDomain.getProductKeyBatchById(id);
        if (batch == null) {
            throw new NotFoundException("product key batch not found");
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
                                                  @RequestParam(value = "is_package", required = false) Boolean isPackage,
                                                  @PageableDefault(page = 0, size = 20)
                                                  @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                                  Pageable pageable,
                                                  HttpServletResponse response) {
        String orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        Page<ProductKeyBatch> productKeyBatchPage;
        productKeyBatchPage = productKeyDomain.getProductKeyBatchesByFilterPaged(orgId, productBaseId, isPackage, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", productKeyBatchPage.toContentRange());
        }
        return productKeyBatchPage.getContent();
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

    @RequestMapping(value = "sum/time", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'filterByOrg', 'productkeybatch:read')")
    public Long sumQuantityTime(
            @RequestParam(value = "org_id", required = false) String orgId,
            @RequestParam(value = "product_base_id", required = false) String productBaseId) {
        if (StringUtils.isEmpty(orgId)) {
            orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }

        DateTime now = DateTime.now();
        DateTime nextMonth = now.plusMonths(1);
        DateTime firstDayOfThisMonth = new DateTime(now.getYear(), now.getMonthOfYear(), 1, 0, 0, 0);
        DateTime lastDayOfThisMonth = new DateTime(nextMonth.getYear(), nextMonth.getMonthOfYear(), 1, 0, 0, 0);

        return productKeyDomain.sumTime(orgId, productBaseId, firstDayOfThisMonth, lastDayOfThisMonth);
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
            ProductBaseObject productBase = productBaseDomain.getProductBaseById(productBaseId);
            if (productBase == null || !orgId.equals(productBase.getOrgId())) { //check orgId of productBase is the same
                throw new BadRequestException("productBaseId invalid");
            }
            if (productKeyTypeCodes == null) {
                productKeyTypeCodes = productBase.getProductKeyTypeCodes();
            }
        }

        if (!productKeyDomain.validateProductKeyTypeCodes(productKeyTypeCodes)) {
            throw new BadRequestException("productKeyTypeCodes invalid");
        }
        batchObj.setBatchNo(request.getBatchNo());
        batchObj.setQuantity(quantity);
        batchObj.setProductBaseId(productBaseId);
        batchObj.setProductKeyTypeCodes(productKeyTypeCodes);
        batchObj.setOrgId(orgId);
        batchObj.setCreatedAppId(appId);
        batchObj.setCreatedAccountId(accountId);
        batchObj.setCreatedDateTime(createdDateTime);
        batchObj.setRestQuantity(quantity);
        log.info(String.format("ProductKeyBatch creating started [quantity: %s]", batchObj.getQuantity()));
        ProductKeyBatch newBatch = productKeyDomain.createProductKeyBatch(batchObj);
        log.info(String.format("ProductKeyBatch created [id: %s, quantity: %s]", newBatch.getId(), newBatch.getQuantity()));

        return newBatch;
    }

    @RequestMapping(value = "product_batch_group", method = RequestMethod.GET)
    public List<ProductBatchCollection> getProductBatchCollection() {
        String orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        Page<ProductBaseObject> pageProductBase = productBaseDomain.getProductBaseByOrgId(orgId, null);
        Page<ProductKeyBatch> pageBatch = productKeyDomain.getProductKeyBatchesByFilterPaged(orgId, null, false, null);


        return pageProductBase.getContent().stream().map(p -> {
            ProductBatchCollection collection = new ProductBatchCollection();
            collection.setProductBase(new ProductBase(p));
            List<ProductKeyBatch> listBatchForProductBase = pageBatch.getContent().stream()
                    .filter(b -> b.getProductBaseId().equals(p.getId()))
                    .sorted((s1, s2) -> s1.getCreatedDateTime().compareTo(s2.getCreatedDateTime()))
                    .collect(Collectors.toList());
            collection.setBatches(listBatchForProductBase);

            return collection;
        }).collect(Collectors.toList());

    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void updateProductKeyBatch(
            @Valid @RequestBody ProductKeyBatch productKeyBatch) {

        if (productKeyBatch == null) {
            throw new BadRequestException("marketing draw record can not be null");
        }
        ProductKeyBatchObject productKeyBatchObject = new ProductKeyBatchObject();

        productKeyBatchObject.setId(productKeyBatch.getId());
        productKeyBatchObject.setBatchNo(productKeyBatch.getBatchNo());
        productKeyBatchObject.setQuantity(productKeyBatch.getQuantity());
        productKeyBatchObject.setStatusCode(productKeyBatch.getStatusCode());
        productKeyBatchObject.setProductKeyTypeCodes(productKeyBatch.getProductKeyTypeCodes());
        productKeyBatchObject.setProductBaseId(productKeyBatch.getProductBaseId());
        productKeyBatchObject.setOrgId(productKeyBatch.getOrgId());
        productKeyBatchObject.setCreatedAppId(productKeyBatch.getCreatedAppId());
        productKeyBatchObject.setCreatedAccountId(productKeyBatch.getCreatedAccountId());
        productKeyBatchObject.setCreatedDateTime(productKeyBatch.getCreatedDateTime());
        productKeyBatchObject.setRestQuantity(productKeyBatch.getQuantity());
        productKeyBatchObject.setMarketingId(productKeyBatch.getMarketingId());

        productKeyDomain.updateProductKeyBatch(productKeyBatchObject);
    }


    @RequestMapping(value = "{id}/details", method = RequestMethod.GET)
    public ResponseEntity<?> getProductKeyBatchDetails(@PathVariable(value = "id") String id) {
        String accountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        String orgId = fixOrgId("current");
        ProductKeyBatch batch = productKeyDomain.getProductKeyBatchById(id);
        if (batch == null) {
            throw new NotFoundException("product key batch not found");
        }
        if (!accountPermissionDomain.hasPermission(accountId, new TPermission(batch.getOrgId(), "productkeybatch", "read"))) {
            throw new ForbiddenException();
        }
        String path = String.format("organization/%s/product_key_batch/%s/details.json", orgId, id);
        ResourceInputStream resourceInputStream = fileDomain.getFile(path);
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON);
        if (resourceInputStream == null) {
            return bodyBuilder.body(null);
        }
        return bodyBuilder.contentLength(resourceInputStream.getContentLength())
                .body(new InputStreamResource(resourceInputStream));
    }

    @RequestMapping(value = "{id}/details", method = RequestMethod.PUT)
    public void putProductKeyBatchDetails(@PathVariable(value = "id") String id,
                                          @RequestBody String details) {
        String accountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        String orgId = fixOrgId("current");
        ProductKeyBatch batch = productKeyDomain.getProductKeyBatchById(id);
        if (batch == null) {
            throw new NotFoundException("product key batch not found");
        }
        if (!accountPermissionDomain.hasPermission(accountId, new TPermission(batch.getOrgId(), "productkeybatch", "modify"))) {
            throw new ForbiddenException();
        }
        String path = String.format("organization/%s/product_key_batch/%s/details.json", orgId, id);
        byte[] bytes = details.getBytes(StandardCharsets.UTF_8);
        fileDomain.putFile(path, new ResourceInputStream(new ByteArrayInputStream(bytes), bytes.length, MediaType.APPLICATION_JSON_VALUE));
    }

    private String fixOrgId(String orgId) {
        if (orgId == null || "current".equals(orgId)) {
            //current orgId
            return tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }
        return orgId;
    }

}

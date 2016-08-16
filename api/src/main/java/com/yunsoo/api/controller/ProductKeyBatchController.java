package com.yunsoo.api.controller;

import com.yunsoo.api.aspect.OperationLog;
import com.yunsoo.api.domain.*;
import com.yunsoo.api.dto.*;
import com.yunsoo.api.security.AuthDetails;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.api.util.PageUtils;
import com.yunsoo.common.data.object.MarketingObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.support.YSFile;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private MarketingDomain marketingDomain;

    @Autowired
    private ProductKeyDomain productKeyDomain;

    @Autowired
    private FileDomain fileDomain;

    @Autowired
    private OrganizationConfigDomain orgConfigDomain;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'product_key_batch:read')")
    public ProductKeyBatch getById(@PathVariable(value = "id") String id) {
        ProductKeyBatch batch = productKeyDomain.getProductKeyBatchById(id);
        if (batch == null) {
            throw new NotFoundException("product key batch not found");
        }
        return batch;
    }

    @RequestMapping(value = "{id}/keys", method = RequestMethod.GET)
    @OperationLog(operation = "'下载产品码批次' + #id", level = "P1")
    public ResponseEntity<?> getKeysById(@PathVariable(value = "id") String id) {
        ProductKeyBatch batch = productKeyDomain.getProductKeyBatchById(id);
        if (batch == null) {
            throw new NotFoundException("product key batch not found " + StringFormatter.formatMap("id", id));
        }

        AuthUtils.checkPermission(batch.getOrgId(), "product_key_batch", "read");

        Map<String, Object> configMap = orgConfigDomain.getConfig(batch.getOrgId(), false, null);
        int downloadNo = (int) configMap.get("enterprise.download_no");
        if (downloadNo <= batch.getDownloadNo())
            throw new BadRequestException("the download number exceed the max download");
        String downloadFileFormat = configMap.get("enterprise.product_key.format").toString();

        byte[] data = productKeyDomain.getProductKeysByBatchId(id);

        ProductKeyBatchObject productKeyBatchObject = new ProductKeyBatchObject();
        productKeyBatchObject.setDownloadNo(batch.getDownloadNo() + 1);
        productKeyBatchObject.setId(batch.getId());
        productKeyDomain.patchUpdateProductKeyBatch(productKeyBatchObject);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd+ys." + downloadFileFormat))
                .contentLength(data.length)
                .header("Content-Disposition", "attachment; filename=\"product_key_batch_" + id + "." + downloadFileFormat + "\"")
                .body(new InputStreamResource(new ByteArrayInputStream(data)));
    }

    @RequestMapping(value = "{id}/file", method = RequestMethod.GET)
    public ResponseEntity<?> getYSFileByProductKeyBatchId(@PathVariable(value = "id") String id) {
        ProductKeyBatch batch = productKeyDomain.getProductKeyBatchById(id);
        if (batch == null) {
            throw new NotFoundException("product key batch not found " + StringFormatter.formatMap("id", id));
        }
        AuthUtils.checkPermission(batch.getOrgId(), "product_key_batch", "read");

        YSFile ysFile = productKeyDomain.getProductKeyFile(id, batch.getOrgId());
        if (ysFile == null) {
            throw new NotFoundException("product key batch file not found " + StringFormatter.formatMap("id", id));
        }
        byte[] data = ysFile.toBytes();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd+ys.pks"))
                .contentLength(data.length)
                .header("Content-Disposition", "attachment; filename=\"product_key_batch_" + id + ".pks\"")
                .body(new InputStreamResource(new ByteArrayInputStream(data)));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PostAuthorize("hasPermission('current', 'org', 'product_key_batch:read')")
    public List<ProductKeyBatch> getByFilterPaged(@RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                  @RequestParam(value = "create_account", required = false) String createAccount,
                                                  @RequestParam(value = "device_id", required = false) String deviceId,
                                                  @RequestParam(value = "create_datetime_start", required = false)
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                                  @RequestParam(value = "create_datetime_end", required = false)
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                                  @RequestParam(value = "is_package", required = false) Boolean isPackage,
                                                  @PageableDefault(page = 0, size = 1000)
                                                  @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                                  Pageable pageable,
                                                  HttpServletResponse response) {
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        Page<ProductKeyBatch> productKeyBatchPage = productKeyDomain.getProductKeyBatchesByFilterPaged(orgId, productBaseId, isPackage, createAccount, deviceId, createdDateTimeStart, createdDateTimeEnd, pageable);

        return PageUtils.response(response, productKeyBatchPage, pageable != null);
    }

    @RequestMapping(value = "sum/quantity", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'product_key_batch:read')")
    public Long sumQuantity(
            @RequestParam(value = "org_id", required = false) String orgId,
            @RequestParam(value = "product_base_id", required = false) String productBaseId) {
        orgId = AuthUtils.fixOrgId(orgId);
        return productKeyDomain.sumQuantity(orgId, productBaseId);
    }

    @RequestMapping(value = "sum/time", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'product_key_batch:read')")
    public Long sumQuantityTime(
            @RequestParam(value = "org_id", required = false) String orgId,
            @RequestParam(value = "product_base_id", required = false) String productBaseId) {
        orgId = AuthUtils.fixOrgId(orgId);

        DateTime now = DateTime.now();
        DateTime nextMonth = now.plusMonths(1);
        DateTime firstDayOfThisMonth = new DateTime(now.getYear(), now.getMonthOfYear(), 1, 0, 0, 0);
        DateTime lastDayOfThisMonth = new DateTime(nextMonth.getYear(), nextMonth.getMonthOfYear(), 1, 0, 0, 0);

        return productKeyDomain.sumTime(orgId, productBaseId, firstDayOfThisMonth, lastDayOfThisMonth);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission('current', 'org', 'product_key_batch:create')")
    @OperationLog(operation = "'创建产品码批次' + #request.productBaseId", level = "P1")
    public ProductKeyBatch create(@Valid @RequestBody ProductKeyBatchRequest request) {
        AuthDetails authDetails = AuthUtils.getAuthDetails();
        String appId = authDetails.getAppId();
        String deviceId = authDetails.getDeviceId();
        int quantity = request.getQuantity();
        String productBaseId = request.getProductBaseId();
        List<String> productKeyTypeCodes = request.getProductKeyTypeCodes();

        String orgId = AuthUtils.getCurrentAccount().getOrgId();

        if (productBaseId != null) {
            //create corresponding product according to the productBaseId
            ProductBaseObject productBase = productBaseDomain.getProductBaseById(productBaseId);
            if (productBase == null || !orgId.equals(productBase.getOrgId())) { //check orgId of productBase is the same
                throw new BadRequestException("product_base_id invalid");
            }
            if (productKeyTypeCodes == null) {
                productKeyTypeCodes = productBase.getProductKeyTypeCodes();
            }
        }

        if (!productKeyDomain.validateProductKeyTypeCodes(productKeyTypeCodes)) {
            throw new BadRequestException("product_key_type_codes invalid");
        }

        ProductKeyBatchObject batchObj = new ProductKeyBatchObject();
        batchObj.setBatchNo(request.getBatchNo());
        batchObj.setQuantity(quantity);
        batchObj.setProductBaseId(productBaseId);
        batchObj.setProductKeyTypeCodes(productKeyTypeCodes);
        batchObj.setOrgId(orgId);
        batchObj.setCreatedAppId(appId);
        batchObj.setCreatedDeviceId(deviceId);
        log.info(String.format("ProductKeyBatch creating started [quantity: %s]", batchObj.getQuantity()));
        ProductKeyBatch newBatch = productKeyDomain.createProductKeyBatch(batchObj);
        log.info(String.format("ProductKeyBatch created [id: %s, quantity: %s]", newBatch.getId(), newBatch.getQuantity()));

        return newBatch;
    }

    @RequestMapping(value = "product_batch_group", method = RequestMethod.GET)
    public List<ProductBatchCollection> getProductBatchCollection() {
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        Page<ProductBaseObject> pageProductBase = productBaseDomain.getProductBaseByOrgId(orgId, null, null, null, null, null);
        Page<ProductKeyBatch> pageBatch = productKeyDomain.getProductKeyBatchesByFilterPaged(orgId, null, false, null, null, null, null, null);


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
    @OperationLog(operation = "'更新产品码批次' + #productKeyBatch.id", level = "P1")
    public void patchUpdateProductKeyBatch(
            @Valid @RequestBody ProductKeyBatch productKeyBatch) {
        ProductKeyBatchObject productKeyBatchObject = new ProductKeyBatchObject();
        productKeyBatchObject.setId(productKeyBatch.getId());
        productKeyBatchObject.setMarketingId(productKeyBatch.getMarketingId());

        productKeyDomain.patchUpdateProductKeyBatch(productKeyBatchObject);
    }

    @RequestMapping(value = "{id}/marketing_id", method = RequestMethod.PUT)
    @OperationLog(operation = "'产品码批次关联营销方案' + #id", level = "P1")
    public void putMarketingId(@PathVariable(value = "id") String id, @RequestBody(required = false) String marketingId) {
        productKeyDomain.putMarketingId(id, marketingId);
        if (marketingId != null) {
            MarketingObject marketingObject = marketingDomain.getMarketingById(marketingId);
            marketingObject.setProductBaseId(productKeyDomain.getProductKeyBatchById(id).getProductBaseId());
            marketingDomain.updateMarketing(marketingObject);
        }
    }


    @RequestMapping(value = "{id}/details", method = RequestMethod.GET)
    public ResponseEntity<?> getProductKeyBatchDetails(@PathVariable(value = "id") String id) {
        ProductKeyBatch batch = productKeyDomain.getProductKeyBatchById(id);
        if (batch == null) {
            throw new NotFoundException("product key batch not found");
        }
        AuthUtils.checkPermission(batch.getOrgId(), "product_key_batch", "read");

        String path = String.format("organization/%s/product_key_batch/%s/details.json", batch.getOrgId(), id);
        ResourceInputStream resourceInputStream = fileDomain.getFile(path);
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON);
        if (resourceInputStream == null) {
            return bodyBuilder.body(null);
        }
        return bodyBuilder.contentLength(resourceInputStream.getContentLength())
                .body(new InputStreamResource(resourceInputStream));
    }

    @RequestMapping(value = "{id}/details", method = RequestMethod.PUT)
    @OperationLog(operation = "'更新产品码批次' + #id", level = "P1")
    public void putProductKeyBatchDetails(@PathVariable(value = "id") String id,
                                          @RequestBody String details) {
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        ProductKeyBatch batch = productKeyDomain.getProductKeyBatchById(id);
        if (batch == null) {
            throw new NotFoundException("product key batch not found");
        }
        AuthUtils.checkPermission(batch.getOrgId(), "product_key_batch", "write");

        String path = String.format("organization/%s/product_key_batch/%s/details.json", orgId, id);
        byte[] bytes = details.getBytes(StandardCharsets.UTF_8);
        fileDomain.putFile(path, new ResourceInputStream(new ByteArrayInputStream(bytes), bytes.length, MediaType.APPLICATION_JSON_VALUE));
    }

    @RequestMapping(value = "/marketing/{id}", method = RequestMethod.GET)
    public List<ProductKeyBatchInfo> getKeybatchInfoByMarketingId(@PathVariable(value = "id") String marketingId) {

        if (marketingId == null)
            throw new BadRequestException("marketing id can not be null");

        List<ProductKeyBatchInfo> productKeyBatchInfoList = new ArrayList<>();

        List<ProductKeyBatch> productKeyBatches = productKeyDomain.getProductKeybatchByMarketingId(marketingId).stream().map(ProductKeyBatch::new).collect(Collectors.toList());
        if ((productKeyBatches != null) && (productKeyBatches.size() > 0)) {
            for (ProductKeyBatch productKeyBatch : productKeyBatches) {
                ProductKeyBatchInfo object = new ProductKeyBatchInfo();
                object.setId(productKeyBatch.getId());
                object.setBatchNo(productKeyBatch.getBatchNo());
                object.setQuantity(productKeyBatch.getQuantity());
                object.setProductBaseId(productKeyBatch.getProductBaseId());
                object.setCreatedDateTime(productKeyBatch.getCreatedDateTime());
                ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(productKeyBatch.getProductBaseId());
                if (productBaseObject != null) {
                    object.setProductBaseName(productBaseObject.getName());
                }
                productKeyBatchInfoList.add(object);

            }
        }
        return productKeyBatchInfoList;
    }

}

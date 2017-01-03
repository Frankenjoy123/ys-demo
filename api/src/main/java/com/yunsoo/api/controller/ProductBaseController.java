package com.yunsoo.api.controller;

import com.yunsoo.api.aspect.OperationLog;
import com.yunsoo.api.domain.*;
import com.yunsoo.api.dto.Lookup;
import com.yunsoo.api.dto.ProductBase;
import com.yunsoo.api.dto.ProductCategory;
import com.yunsoo.api.file.service.FileService;
import com.yunsoo.api.file.service.ImageService;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.api.util.PageUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductBaseVersionsObject;
import com.yunsoo.common.data.object.ProductCategoryObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2015/3/20
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/productbase")
public class ProductBaseController {

    @Autowired
    private ProductBaseDomain productBaseDomain;

    @Autowired
    private ProductCategoryDomain productCategoryDomain;

    @Autowired
    private LookupDomain lookupDomain;

    @Autowired
    private UserFollowingDomain followingDomain;

    @Autowired
    private FileService fileService;

    @Autowired
    private ImageService imageService;

    //region product base

    /**
     * @param productBaseId id of product base
     * @return product base
     */
    @RequestMapping(value = "{product_base_id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'product_base:read')")
    public ProductBase getById(@PathVariable(value = "product_base_id") String productBaseId) {
        ProductBaseObject productBaseObject = findProductBaseById(productBaseId);

        ProductBase productBase = new ProductBase(productBaseObject);
        productBase.setCategory(new ProductCategory(productCategoryDomain.getById(productBase.getCategoryId())));
        productBase.setProductKeyTypes(Lookup.fromCodeList(lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyType), productBaseObject.getProductKeyTypeCodes()));
        productBase.setFollowingUsers(followingDomain.getFollowingUsersByProductBaseId(productBaseId, null).getContent());
        return productBase;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'product_base:read')")
    public List<ProductBase> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                         @RequestParam(value = "pro_name", required = false) String proName,
                                         @RequestParam(value = "create_account", required = false) String createAccount,
                                         @RequestParam(value = "pro_ids", required = false) List<String> productBaseIds,
                                         @RequestParam(value = "create_datetime_start", required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                         @RequestParam(value = "create_datetime_end", required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                         @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                         Pageable pageable,
                                         HttpServletResponse response) {
        orgId = AuthUtils.fixOrgId(orgId);
        Page<ProductBaseObject> productBasePage = productBaseDomain.getProductBaseByOrgId(orgId, pageable, proName, createAccount, createdDateTimeStart, createdDateTimeEnd, productBaseIds);

        Map<String, ProductCategoryObject> productCategoryObjectMap = productCategoryDomain.getProductCategoryMap();
        List<Lookup> lookupList = lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyType);
        return PageUtils.response(response, productBasePage.map(p -> {
            ProductBase pb = new ProductBase(p);
            pb.setCategory(new ProductCategory(productCategoryDomain.getById(p.getCategoryId(), productCategoryObjectMap)));
            pb.setProductKeyTypes(Lookup.fromCodeList(lookupList, p.getProductKeyTypeCodes()));
            return pb;
        }), pageable != null);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'product_base:read')")
    public Long getCount(@RequestParam(value = "org_id", required = false) String orgId) {

        orgId = AuthUtils.fixOrgId(orgId);
        Long count = productBaseDomain.getProductBaseCountByOrgId(orgId);

        return count;
    }

    //create product base
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#productBase.orgId, 'org', 'product_base:create')")
    @OperationLog(operation = "'保存产品方案:' + #productBase.name", level = "P1")
    public ProductBase create(@RequestBody ProductBase productBase) {
        ProductBaseObject productBaseObject = new ProductBaseObject();
        ProductBaseVersionsObject productBaseVersionsObject = new ProductBaseVersionsObject();
        productBaseObject.setName(productBase.getName());
        productBaseObject.setWebTemplateName(productBase.getWebTemplateName());
        productBaseObject.setWebTemplateVersion(productBase.getWebTemplateVersion());
        productBaseObject.setVersion(1);
        String currentAccountId = AuthUtils.getCurrentAccount().getId();

        if (StringUtils.hasText(productBase.getOrgId()))
            productBaseObject.setOrgId(productBase.getOrgId());
        else
            productBaseObject.setOrgId(AuthUtils.getCurrentAccount().getOrgId());

        productBaseObject.setBarcode(productBase.getBarcode());
        productBaseObject.setVersion(1);
        productBaseObject.setStatusCode(LookupCodes.ProductBaseStatus.ACTIVATED);
        productBaseObject.setCategoryId(productBase.getCategoryId());
        productBaseObject.setChildProductCount(productBase.getChildProductCount());
        productBaseObject.setComments(productBase.getComments());
        productBaseObject.setCreatedAccountId(currentAccountId);
        productBaseObject.setCreatedDateTime(DateTime.now());
        productBaseObject.setId(productBase.getId());
        productBaseObject.setModifiedDateTime(productBase.getModifiedDateTime());
        productBaseObject.setProductKeyTypeCodes(productBase.getProductKeyTypeCodes());
        productBaseObject.setShelfLife(productBase.getShelfLife());
        productBaseObject.setShelfLifeInterval(productBase.getShelfLifeInterval());
        productBaseObject.setImage(productBase.getImage());

        ProductBaseObject newProductBaseObject = productBaseDomain.createProductBase(productBaseObject);
        String id = newProductBaseObject.getId();
        productBaseVersionsObject.setProductBase(productBaseObject);
        productBaseVersionsObject.setProductBaseId(id);
        productBaseVersionsObject.setVersion(1);
        productBaseVersionsObject.setStatusCode(LookupCodes.ProductBaseVersionsStatus.SUBMITTED);
        productBaseVersionsObject.setCreatedAccountId(currentAccountId);
        productBaseVersionsObject.setCreatedDateTime(DateTime.now());
        productBaseDomain.createProductBaseVersions(productBaseVersionsObject);
        return new ProductBase(newProductBaseObject);
    }


    //update product base versions: edit current product version detail
    @RequestMapping(value = "{product_base_id}", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#productBase.orgId, 'org', 'product_base:write')")
    @OperationLog(operation = "'修改产品方案'+#productBase.name", level = "P1")
    public void updateProductBase(@PathVariable(value = "product_base_id") String productBaseId,
                                  @RequestBody ProductBase productBase) {
        ProductBaseObject originalProductBaseObject = findProductBaseById(productBaseId);
        Integer version = originalProductBaseObject.getVersion();

        ProductBaseObject productBaseObject = new ProductBaseObject();
        ProductBaseVersionsObject productBaseVersionsObject = new ProductBaseVersionsObject();

        productBaseObject.setId(productBaseId);
        productBaseObject.setVersion(version);
        productBaseObject.setName(productBase.getName());
        productBaseObject.setWebTemplateVersion(productBase.getWebTemplateVersion());
        productBaseObject.setWebTemplateName(productBase.getWebTemplateName());
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        productBaseObject.setOrgId(StringUtils.hasText(productBase.getOrgId())
                ? productBase.getOrgId()
                : AuthUtils.getCurrentAccount().getOrgId());
        productBaseObject.setBarcode(productBase.getBarcode());
        productBaseObject.setStatusCode(productBase.getStatusCode());
        productBaseObject.setCategoryId(productBase.getCategoryId());
        productBaseObject.setChildProductCount(productBase.getChildProductCount());
        productBaseObject.setComments(productBase.getComments());
        productBaseObject.setCreatedDateTime(productBase.getCreatedDateTime());
        productBaseObject.setModifiedDateTime(productBase.getModifiedDateTime());
        productBaseObject.setProductKeyTypeCodes(productBase.getProductKeyTypeCodes());
        productBaseObject.setShelfLife(productBase.getShelfLife());
        productBaseObject.setShelfLifeInterval(productBase.getShelfLifeInterval());
        productBaseObject.setImage(productBase.getImage());

        productBaseVersionsObject.setProductBase(productBaseObject);
        productBaseVersionsObject.setProductBaseId(productBaseId);
        productBaseVersionsObject.setVersion(version);
        productBaseVersionsObject.setStatusCode(LookupCodes.ProductBaseVersionsStatus.SUBMITTED);
        productBaseVersionsObject.setCreatedAccountId(currentAccountId);
        productBaseVersionsObject.setCreatedDateTime(DateTime.now());
        productBaseDomain.patchUpdateProductBaseVersions(productBaseVersionsObject);
        productBaseDomain.patchUpdateProductBase(productBaseObject);
    }

    /**
     * delete product base or specified editable version
     *
     * @param productBaseId id of product base
     */
    @RequestMapping(value = "{product_base_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @OperationLog(operation = "'删除产品方案:' + #productBaseId", level = "P1")
    public void delete(@PathVariable(value = "product_base_id") String productBaseId) {
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(productBaseId);
        if (productBaseObject != null) {
            //delete product base
            AuthUtils.checkPermission(productBaseObject.getOrgId(), "product_base", "delete");

            productBaseDomain.deleteProductBase(productBaseId);
        }
    }

    //endregion


    //region product base image

    @RequestMapping(value = "{product_base_id}/image", method = RequestMethod.GET)
    public ResponseEntity<?> getProductBaseImage(
            @PathVariable(value = "product_base_id") String productBaseId) {
        ProductBaseObject productBaseObject = findProductBaseById(productBaseId);
        String imageName = productBaseObject.getImage();
        if (imageName == null) {
            throw new NotFoundException("image not found");
        }
        return imageService.getImage(String.format("image/%s", imageName));
    }


    //endregion


    //region product base details

    @RequestMapping(value = "{product_base_id}/details", method = RequestMethod.GET)
    public ResponseEntity<?> getProductBaseTemplate(@PathVariable(value = "product_base_id") String productBaseId) {
        ProductBaseObject productBaseObject = findProductBaseById(productBaseId);
        Integer version = productBaseObject.getVersion();
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        String path = String.format("organization/%s/product_base/%s/%s/details.json", orgId, productBaseId, version);
        ResourceInputStream resourceInputStream = fileService.getFile(path);
        if (resourceInputStream == null) {
            throw new NotFoundException("product base details not found");
        }
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        bodyBuilder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
        bodyBuilder.contentLength(resourceInputStream.getContentLength());
        return bodyBuilder.body(new InputStreamResource(resourceInputStream));
    }

    @RequestMapping(value = "{product_base_id}/details", method = RequestMethod.PUT)
    @OperationLog(operation = "'修改产品方案详情:'+#productBaseId", level = "P1")
    public void saveProductBaseTemplate(@PathVariable(value = "product_base_id") String productBaseId,
                                        @RequestBody String details) {
        ProductBaseObject productBaseObject = findProductBaseById(productBaseId);
        Integer version = productBaseObject.getVersion();
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        String path = String.format("organization/%s/product_base/%s/%s/details.json", orgId, productBaseId, version);
        ProductBaseVersionsObject productBaseVersionsObject = productBaseDomain.getProductBaseVersionsByProductBaseIdAndVersion(productBaseId, version);
        if (productBaseVersionsObject == null) {
            throw new NotFoundException("specific version of product base not found");
        }
        byte[] bytes = details.getBytes(StandardCharsets.UTF_8);
        fileService.putFile(path, new ResourceInputStream(new ByteArrayInputStream(bytes), bytes.length, MediaType.APPLICATION_JSON_VALUE));
    }

    //endregion


    private ProductBaseObject findProductBaseById(String id) {
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(id);
        if (productBaseObject == null) {
            throw new NotFoundException("product base not found by [id: " + id + "]");
        }
        return productBaseObject;
    }
}

package com.yunsoo.api.controller;

import com.yunsoo.api.domain.*;
import com.yunsoo.api.dto.Lookup;
import com.yunsoo.api.dto.ProductBase;
import com.yunsoo.api.dto.ProductCategory;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductBaseVersionsObject;
import com.yunsoo.common.data.object.ProductCategoryObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.NotFoundException;
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
    private AccountPermissionDomain accountPermissionDomain;

    @Autowired
    private ProductCategoryDomain productCategoryDomain;

    @Autowired
    private LookupDomain lookupDomain;

    @Autowired
    private UserFollowingDomain followingDomain;

    @Autowired
    private FileDomain fileDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;


    //region product base

    /**
     * @param productBaseId id of product base
     * @return product base
     */
    @RequestMapping(value = "{product_base_id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'productbase:read')")
    public ProductBase getById(@PathVariable(value = "product_base_id") String productBaseId) {
        ProductBaseObject productBaseObject = findProductBaseById(productBaseId);

        ProductBase productBase = new ProductBase(productBaseObject);
        productBase.setCategory(new ProductCategory(productCategoryDomain.getById(productBase.getCategoryId())));
        productBase.setProductKeyTypes(Lookup.fromCodeList(lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyType), productBaseObject.getProductKeyTypeCodes()));
        productBase.setFollowingUsers(followingDomain.getFollowingUsersByProductBaseId(productBaseId, null).getContent());
        return productBase;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'productbase:read')")
    public List<ProductBase> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                         @PageableDefault(page = 0, size = 20)
                                         @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                         Pageable pageable,
                                         HttpServletResponse response) {
        orgId = fixOrgId(orgId);
        Page<ProductBaseObject> productBasePage = productBaseDomain.getProductBaseByOrgId(orgId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", productBasePage.toContentRange());
        }
        Map<String, ProductCategoryObject> productCategoryObjectMap = productCategoryDomain.getProductCategoryMap();
        List<Lookup> lookupList = lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyType);
        List<ProductBase> productBases = productBasePage.map(p -> {
            ProductBase pb = new ProductBase(p);
            pb.setCategory(new ProductCategory(productCategoryDomain.getById(p.getCategoryId(), productCategoryObjectMap)));
            pb.setProductKeyTypes(Lookup.fromCodeList(lookupList, p.getProductKeyTypeCodes()));
            return pb;
        }).getContent();

        productBases.forEach(productBase -> productBase.setFollowingUsersTotalNumber(followingDomain.getFollowingUsersCountByProductBaseId(productBase.getId())));

        return productBases;
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'productbase:read')")
    public Long getCount(@RequestParam(value = "org_id", required = false) String orgId) {

        orgId = fixOrgId(orgId);
        Long count = productBaseDomain.getProductBaseCountByOrgId(orgId);

        return count;
    }

    //create product base
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#productBase.orgId, 'org', 'productbase:create')")
    public ProductBase create(@RequestBody ProductBase productBase) {
        ProductBaseObject productBaseObject = new ProductBaseObject();
        ProductBaseVersionsObject productBaseVersionsObject = new ProductBaseVersionsObject();
        productBaseObject.setName(productBase.getName());
        productBaseObject.setVersion(1);
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();

        if (StringUtils.hasText(productBase.getOrgId()))
            productBaseObject.setOrgId(productBase.getOrgId());
        else
            productBaseObject.setOrgId(tokenAuthenticationService.getAuthentication().getDetails().getOrgId());

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
    @PreAuthorize("hasPermission(#productBase.orgId, 'org', 'productbase:modify')")
    public void updateProductBase(@PathVariable(value = "product_base_id") String productBaseId,
                                  @RequestBody ProductBase productBase) {
        ProductBaseObject originalProductBaseObject = findProductBaseById(productBaseId);
        Integer version = originalProductBaseObject.getVersion();

        ProductBaseObject productBaseObject = new ProductBaseObject();
        ProductBaseVersionsObject productBaseVersionsObject = new ProductBaseVersionsObject();

        productBaseObject.setId(productBaseId);
        productBaseObject.setVersion(version);
        productBaseObject.setName(productBase.getName());
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        productBaseObject.setOrgId(StringUtils.hasText(productBase.getOrgId())
                ? productBase.getOrgId()
                : tokenAuthenticationService.getAuthentication().getPrincipal().getOrgId());
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
    public void delete(@PathVariable(value = "product_base_id") String productBaseId) {
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(productBaseId);
        if (productBaseObject != null) {
            //delete product base
            TPermission tPermission = new TPermission(productBaseObject.getOrgId(), "productbase", "delete");
            if (!accountPermissionDomain.hasPermission(tokenAuthenticationService.getAuthentication().getPrincipal().getId(), tPermission)) {
                throw new ForbiddenException("operation denied");
            }
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
        ResourceInputStream resourceInputStream = fileDomain.getFile(String.format("image/%s", imageName));
        if (resourceInputStream == null) {
            throw new NotFoundException("image not found");
        }
        String contentType = resourceInputStream.getContentType();
        long contentLength = resourceInputStream.getContentLength();

        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        if (contentType != null) {
            bodyBuilder.contentType(MediaType.parseMediaType(contentType));
        }
        if (contentLength > 0) {
            bodyBuilder.contentLength(contentLength);
        }
        return bodyBuilder.body(new InputStreamResource(resourceInputStream));
    }


    //endregion


    //region product base details

    @RequestMapping(value = "{product_base_id}/details", method = RequestMethod.GET)
    public ResponseEntity<?> getProductBaseTemplate(@PathVariable(value = "product_base_id") String productBaseId) {
        ProductBaseObject productBaseObject = findProductBaseById(productBaseId);
        Integer version = productBaseObject.getVersion();
        String orgId = fixOrgId("current");
        String path = String.format("organization/%s/product_base/%s/%s/details.json", orgId, productBaseId, version);
        ResourceInputStream resourceInputStream = fileDomain.getFile(path);
        if (resourceInputStream == null) {
            throw new NotFoundException("product base details not found");
        }
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        bodyBuilder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
        bodyBuilder.contentLength(resourceInputStream.getContentLength());
        return bodyBuilder.body(new InputStreamResource(resourceInputStream));
    }

    @RequestMapping(value = "{product_base_id}/details", method = RequestMethod.PUT)
    public void saveProductBaseTemplate(@PathVariable(value = "product_base_id") String productBaseId,
                                        @RequestBody String details) {
        ProductBaseObject productBaseObject = findProductBaseById(productBaseId);
        Integer version = productBaseObject.getVersion();
        String orgId = fixOrgId("current");
        String path = String.format("organization/%s/product_base/%s/%s/details.json", orgId, productBaseId, version);
        ProductBaseVersionsObject productBaseVersionsObject = productBaseDomain.getProductBaseVersionsByProductBaseIdAndVersion(productBaseId, version);
        if (productBaseVersionsObject == null) {
            throw new NotFoundException("specific version of product base not found");
        }
        byte[] bytes = details.getBytes(StandardCharsets.UTF_8);
        fileDomain.putFile(path, new ResourceInputStream(new ByteArrayInputStream(bytes), bytes.length, MediaType.APPLICATION_JSON_VALUE));
    }

    //endregion


    private String fixOrgId(String orgId) {
        if (orgId == null || "current".equals(orgId)) {
            //current orgId
            return tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }
        return orgId;
    }

    private ProductBaseObject findProductBaseById(String id) {
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(id);
        if (productBaseObject == null) {
            throw new NotFoundException("product base not found by [id: " + id + "]");
        }
        return productBaseObject;
    }
}

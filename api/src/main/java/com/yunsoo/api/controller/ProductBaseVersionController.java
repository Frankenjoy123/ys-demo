package com.yunsoo.api.controller;

import com.yunsoo.api.domain.*;
import com.yunsoo.api.dto.Lookup;
import com.yunsoo.api.dto.ProductBase;
import com.yunsoo.api.dto.ProductBaseVersions;
import com.yunsoo.api.dto.ProductCategory;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.api.util.PageUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductBaseVersionsObject;
import com.yunsoo.common.data.object.ProductCategoryObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
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
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-03-02
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/productbaseVersion")
public class ProductBaseVersionController {

    @Autowired
    private ProductBaseDomain productBaseDomain;

    @Autowired
    private ProductCategoryDomain productCategoryDomain;

    @Autowired
    private LookupDomain lookupDomain;

    @Autowired
    private UserFollowingDomain followingDomain;

    @Autowired
    private FileDomain fileDomain;

    public static final String APPROVED = "approved";
    public static final String REJECTED = "rejected";

    //region product base

    /**
     * @param productBaseId id of product base
     * @param version       it will be version of current product base if it's null
     * @return product base
     */
    @RequestMapping(value = "{product_base_id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'product_base:read')")
    public ProductBase getById(@PathVariable(value = "product_base_id") String productBaseId,
                               @RequestParam(value = "version", required = false) Integer version) {
        ProductBaseObject productBaseObject = findProductBaseById(productBaseId);
        String orgId = productBaseObject.getOrgId();
        if (version == null) {
            version = productBaseObject.getVersion();
        } else {
            ProductBaseVersionsObject productBaseVersionsObject = productBaseDomain.getProductBaseVersionsByProductBaseIdAndVersion(productBaseId, version);
            if (productBaseVersionsObject == null) {
                throw new NotFoundException("product base not found on the specific version");
            }
            productBaseObject = productBaseVersionsObject.getProductBase();
        }

        ProductBase productBase = new ProductBase(productBaseObject);
        productBase.setCategory(new ProductCategory(productCategoryDomain.getById(productBase.getCategoryId())));
        productBase.setProductKeyTypes(Lookup.fromCodeList(lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyType), productBaseObject.getProductKeyTypeCodes()));
        productBase.setFollowingUsers(followingDomain.getFollowingUsersByProductBaseId(productBaseId, null).getContent());
        return productBase;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'product_base:read')")
    public List<ProductBase> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                         Pageable pageable,
                                         HttpServletResponse response) {
        orgId = AuthUtils.fixOrgId(orgId);
        Page<ProductBaseObject> productBasePage = productBaseDomain.getProductBaseByOrgId(orgId, pageable, null, null, null, null);

        Map<String, ProductCategoryObject> productCategoryObjectMap = productCategoryDomain.getProductCategoryMap();
        List<Lookup> lookupList = lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyType);

        List<ProductBase> productBases = PageUtils.response(response, productBasePage.map(p -> {
            ProductBase pb = new ProductBase(p);
            pb.setCategory(new ProductCategory(productCategoryDomain.getById(p.getCategoryId(), productCategoryObjectMap)));
            pb.setProductKeyTypes(Lookup.fromCodeList(lookupList, p.getProductKeyTypeCodes()));
            return pb;
        }), pageable != null);

        List<String> productBaseIds = productBases.stream().map(ProductBase::getId).collect(Collectors.toList());
        Map<String, List<ProductBaseVersionsObject>> productbaseVersionsMap = productBaseDomain.getProductBaseVersionsByProductBaseIds(productBaseIds);
        for (ProductBase pb : productBases) {
            List<ProductBaseVersionsObject> productBaseVersionsObjects = productbaseVersionsMap.get(pb.getId());
            if (productBaseVersionsObjects != null) {
                pb.setProductBaseVersions(productBaseVersionsObjects.stream().map(ProductBaseVersions::new).collect(Collectors.toList()));
            }
        }

        return productBases;
    }


    //create product base
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#productBase.orgId, 'org', 'product_base:create')")
    public ProductBase create(@RequestBody ProductBase productBase) {
        ProductBaseObject productBaseObject = new ProductBaseObject();
        ProductBaseVersionsObject productBaseVersionsObject = new ProductBaseVersionsObject();
        productBaseObject.setName(productBase.getName());
        productBaseObject.setVersion(1);
        String currentAccountId = AuthUtils.getCurrentAccount().getId();

        if (StringUtils.hasText(productBase.getOrgId()))
            productBaseObject.setOrgId(productBase.getOrgId());
        else
            productBaseObject.setOrgId(AuthUtils.getCurrentAccount().getOrgId());

        productBaseObject.setBarcode(productBase.getBarcode());
        productBaseObject.setVersion(1);
        productBaseObject.setStatusCode(LookupCodes.ProductBaseStatus.CREATED);
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

    //create new product base versions
    @RequestMapping(value = "{product_base_id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#productBase.orgId, 'org', 'product_base:write')")
    public ProductBaseVersionsObject createProductBaseVersions(@PathVariable(value = "product_base_id") String productBaseId,
                                                               @RequestBody ProductBase productBase) {
        ProductBaseObject originalProductBaseObject = findProductBaseById(productBaseId);
        if (originalProductBaseObject == null) {
            throw new NotFoundException("product base not found");
        }
        ProductBaseVersionsObject latestProductBaseVersionsObject = productBaseDomain.getLatestProductBaseVersionsByProductBaseId(productBaseId);
        if ((latestProductBaseVersionsObject == null) || (!LookupCodes.ProductBaseStatus.ACTIVATED.equals(originalProductBaseObject.getStatusCode())) || (!LookupCodes.ProductBaseVersionsStatus.ACTIVATED.equals(latestProductBaseVersionsObject.getStatusCode()))) {
            throw new BadRequestException("Not allow to create new product base version on the current product base id");
        }
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        ProductBaseVersionsObject productBaseVersionsObject = new ProductBaseVersionsObject();
        ProductBaseObject productBaseObject = new ProductBaseObject();

        productBaseVersionsObject.setProductBaseId(productBaseId);
        productBaseVersionsObject.setVersion(originalProductBaseObject.getVersion() + 1);
        productBaseVersionsObject.setStatusCode(LookupCodes.ProductBaseVersionsStatus.SUBMITTED);
        productBaseVersionsObject.setCreatedAccountId(currentAccountId);
        productBaseVersionsObject.setCreatedDateTime(DateTime.now());

        productBaseObject.setId(productBaseId);
        productBaseObject.setName(productBase.getName());
        productBaseObject.setVersion(originalProductBaseObject.getVersion() + 1);
        if (StringUtils.hasText(productBase.getOrgId()))
            productBaseObject.setOrgId(productBase.getOrgId());
        else
            productBaseObject.setOrgId(AuthUtils.getCurrentAccount().getOrgId());
        productBaseObject.setBarcode(productBase.getBarcode());
        productBaseObject.setStatusCode(LookupCodes.ProductBaseStatus.CREATED);
        productBaseObject.setCategoryId(productBase.getCategoryId());
        productBaseObject.setChildProductCount(productBase.getChildProductCount());
        productBaseObject.setComments(productBase.getComments());
        productBaseObject.setCreatedAccountId(currentAccountId);
        productBaseObject.setCreatedDateTime(DateTime.now());
        productBaseObject.setModifiedDateTime(productBase.getModifiedDateTime());
        productBaseObject.setProductKeyTypeCodes(productBase.getProductKeyTypeCodes());
        productBaseObject.setShelfLife(productBase.getShelfLife());
        productBaseObject.setShelfLifeInterval(productBase.getShelfLifeInterval());
        productBaseVersionsObject.setProductBase(productBaseObject);

        return productBaseDomain.createProductBaseVersions(productBaseVersionsObject);
    }

    //update product base versions: edit current product version detail
    @RequestMapping(value = "{product_base_id}", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#productBase.orgId, 'org', 'product_base:write')")
    public void updateProductBaseVersions(@PathVariable(value = "product_base_id") String productBaseId,
                                          @RequestParam(value = "version", required = false) Integer version,
                                          @RequestBody ProductBase productBase) {
        ProductBaseObject originalProductBaseObject = findProductBaseById(productBaseId);
        Integer currentVersion = originalProductBaseObject.getVersion();
        if (version == null) {
            version = currentVersion;
        }
        List<ProductBaseVersionsObject> productBaseVersionsObjects = productBaseDomain.getProductBaseVersionsByProductBaseId(productBaseId);
        if (productBaseVersionsObjects.size() == 0) {
            throw new NotFoundException("product base version not found");
        }

        ProductBaseObject productBaseObject = new ProductBaseObject();
        ProductBaseVersionsObject productBaseVersionsObject = new ProductBaseVersionsObject();

        productBaseObject.setId(productBaseId);
        productBaseObject.setVersion(version);
        productBaseObject.setName(productBase.getName());
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        if (StringUtils.hasText(productBase.getOrgId()))
            productBaseObject.setOrgId(productBase.getOrgId());
        else
            productBaseObject.setOrgId(AuthUtils.getCurrentAccount().getOrgId());

        productBaseObject.setBarcode(productBase.getBarcode());
        productBaseObject.setStatusCode(productBase.getStatusCode());
        productBaseObject.setCategoryId(productBase.getCategoryId());
        productBaseObject.setChildProductCount(productBase.getChildProductCount());
        productBaseObject.setComments(productBase.getComments());
        productBaseObject.setCreatedDateTime(productBase.getCreatedDateTime());
        productBaseObject.setId(productBase.getId());
        productBaseObject.setModifiedDateTime(productBase.getModifiedDateTime());
        productBaseObject.setProductKeyTypeCodes(productBase.getProductKeyTypeCodes());
        productBaseObject.setShelfLife(productBase.getShelfLife());
        productBaseObject.setShelfLifeInterval(productBase.getShelfLifeInterval());

        productBaseVersionsObject.setProductBase(productBaseObject);
        productBaseVersionsObject.setProductBaseId(productBaseId);
        productBaseVersionsObject.setVersion(version);
        productBaseVersionsObject.setStatusCode(LookupCodes.ProductBaseVersionsStatus.SUBMITTED);
        productBaseVersionsObject.setCreatedAccountId(currentAccountId);
        productBaseVersionsObject.setCreatedDateTime(DateTime.now());
        productBaseDomain.updateProductBaseVersions(productBaseVersionsObject);
    }

    //approve/reject product base versions
    @RequestMapping(value = "{product_base_id}/approval", method = RequestMethod.PATCH)
    public void approveProductBaseVersions(@PathVariable(value = "product_base_id") String productBaseId,
                                           @RequestParam(value = "version", required = false) Integer version,
                                           @RequestParam(value = "approval_status") String approvalStatus,
                                           @RequestParam(value = "review_comments", required = false) String reviewComments) {
        ProductBaseVersionsObject originalProductBaseVersionsObject = productBaseDomain.getLatestProductBaseVersionsByProductBaseId(productBaseId);
        ProductBaseObject originalProductBaseObject = productBaseDomain.getProductBaseById(productBaseId);
        if ((originalProductBaseVersionsObject == null) || (originalProductBaseObject == null)) {
            throw new NotFoundException("product base or product base version not found");
        }
        if (version == null) {
            version = originalProductBaseVersionsObject.getVersion();
        }
        ProductBaseVersionsObject productBaseVersionsObject = productBaseDomain.getProductBaseVersionsByProductBaseIdAndVersion(productBaseId, version);
        if (productBaseVersionsObject == null) {
            throw new NotFoundException("product base version not found");
        }
        if (!LookupCodes.ProductBaseVersionsStatus.SUBMITTED.equals(productBaseVersionsObject.getStatusCode())) {
            throw new UnprocessableEntityException("illegal operation");
        }
        if (APPROVED.equals(approvalStatus)) {
            productBaseVersionsObject.setStatusCode(LookupCodes.ProductBaseVersionsStatus.ACTIVATED);
            if (reviewComments != null) {
                productBaseVersionsObject.setReviewComments(reviewComments);
            }
            productBaseDomain.updateProductBaseVersions(productBaseVersionsObject);
            ProductBaseObject productBaseObject = productBaseDomain.copyFromProductBaseVersionsObject(productBaseVersionsObject);
            productBaseDomain.updateProductBase(productBaseObject);
        }
        if (REJECTED.equals(approvalStatus)) {
            productBaseVersionsObject.setStatusCode(LookupCodes.ProductBaseVersionsStatus.REJECTED);
            if (reviewComments != null) {
                productBaseVersionsObject.setReviewComments(reviewComments);
            }
            productBaseDomain.updateProductBaseVersions(productBaseVersionsObject);
        }
    }

    /**
     * delete product base or specified editable version
     *
     * @param productBaseId id of product base
     * @param version       if version is null delete the product base, else delete the specified version
     */
    @RequestMapping(value = "{product_base_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "product_base_id") String productBaseId,
                       @RequestParam(value = "version", required = false) Integer version) {
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(productBaseId);
        if (productBaseObject != null) {
            if (version == null) {
                //delete product base
                AuthUtils.checkPermission(productBaseObject.getOrgId(), "productbase", "delete");
                productBaseDomain.deleteProductBase(productBaseId);
            } else {
                AuthUtils.checkPermission(productBaseObject.getOrgId(), "productbase", "write");
                ProductBaseVersionsObject productBaseVersionsObject = productBaseDomain.getProductBaseVersionsByProductBaseIdAndVersion(productBaseId, version);
                if (productBaseVersionsObject != null) {
                    if (!LookupCodes.ProductBaseVersionsStatus.EDITABLE_STATUS.contains(productBaseVersionsObject.getStatusCode())) {
                        throw new UnprocessableEntityException("illegal operation");
                    }
                    productBaseDomain.deleteProductBaseVersions(productBaseId, version);
                }
            }
        }
    }

    //endregion


    //region product base image

    @RequestMapping(value = "{product_base_id}/image", method = RequestMethod.GET)
    public ResponseEntity<?> getProductBaseImage(
            @PathVariable(value = "product_base_id") String productBaseId,
            @RequestParam(value = "version", required = false) Integer version) {
        ProductBaseObject productBaseObject = findProductBaseById(productBaseId);
        Integer currentVersion = productBaseObject.getVersion();
        if (version != null && !version.equals(currentVersion)) {
            findProductBaseVersionsById(productBaseId, version);
        }
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
    public ResponseEntity<?> getProductBaseDetails(@PathVariable(value = "product_base_id") String productBaseId,
                                                   @RequestParam(value = "version", required = false) Integer version) {
        ProductBaseObject productBaseObject = findProductBaseById(productBaseId);
        if (version == null) {
            version = productBaseObject.getVersion();
        }
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
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
    public void saveProductBaseDetails(@PathVariable(value = "product_base_id") String productBaseId,
                                       @RequestParam(value = "version", required = false) Integer version,
                                       @RequestBody String details) {
        ProductBaseObject productBaseObject = findProductBaseById(productBaseId);
        if (version == null) {
            version = productBaseObject.getVersion();
        }
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        String path = String.format("organization/%s/product_base/%s/%s/details.json", orgId, productBaseId, version);
        ProductBaseVersionsObject productBaseVersionsObject = productBaseDomain.getProductBaseVersionsByProductBaseIdAndVersion(productBaseId, version);
        if (productBaseVersionsObject == null) {
            throw new NotFoundException("specific version of product base not found");
        }
        byte[] bytes = details.getBytes(StandardCharsets.UTF_8);
        fileDomain.putFile(path, new ResourceInputStream(new ByteArrayInputStream(bytes), bytes.length, MediaType.APPLICATION_JSON_VALUE));
    }

    //endregion

    private ProductBaseObject findProductBaseById(String productBaseId) {
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(productBaseId);
        if (productBaseObject == null) {
            throw new NotFoundException("product base not found by [id: " + productBaseId + "]");
        }
        return productBaseObject;
    }

    private ProductBaseVersionsObject findProductBaseVersionsById(String productBaseId, Integer version) {
        ProductBaseVersionsObject productBaseVersionsObject = productBaseDomain.getProductBaseVersionsByProductBaseIdAndVersion(productBaseId, version);
        if (productBaseVersionsObject == null) {
            throw new NotFoundException("product base versions not found");
        }
        return productBaseVersionsObject;
    }

}

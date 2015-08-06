package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountPermissionDomain;
import com.yunsoo.api.domain.LookupDomain;
import com.yunsoo.api.domain.ProductBaseDomain;
import com.yunsoo.api.domain.ProductCategoryDomain;
import com.yunsoo.api.dto.*;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.LookupObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductBaseVersionsObject;
import com.yunsoo.common.data.object.ProductCategoryObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private TokenAuthenticationService tokenAuthenticationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductBaseController.class);

    //region product base

    /**
     * @param productBaseId id of product base
     * @param version       it will be version of current product base if it's null
     * @return product base
     * @throws IOException
     */
    @RequestMapping(value = "{product_base_id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'productbase:read')")
    public ProductBase getById(@PathVariable(value = "product_base_id") String productBaseId,
                               @RequestParam(value = "version", required = false) Integer version) throws IOException {
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
        productBase.setProductKeyTypes(LookupObject.fromCodeList(lookupDomain.getProductKeyTypes(), productBaseObject.getProductKeyTypeCodes()));
        productBase.setDetails(productBaseDomain.getProductBaseDetails(orgId, productBaseId, version));
        return productBase;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'productbase:read')")
    public List<ProductBase> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                         Pageable pageable,
                                         HttpServletResponse response) {
        orgId = fixOrgId(orgId);
        Page<ProductBaseObject> productBasePage = productBaseDomain.getProductBaseByOrgId(orgId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", productBasePage.toContentRange());
        }
        Map<String, ProductCategoryObject> productCategoryObjectMap = productCategoryDomain.getProductCategoryMap();
        List<ProductKeyType> productKeyTypes = lookupDomain.getProductKeyTypes();
        List<ProductBase> productBases = productBasePage.map(p -> {
            ProductBase pb = new ProductBase(p);
            pb.setCategory(new ProductCategory(productCategoryDomain.getById(p.getCategoryId(), productCategoryObjectMap)));
            pb.setProductKeyTypes(LookupObject.fromCodeList(productKeyTypes, p.getProductKeyTypeCodes()));
            return pb;
        }).getContent();

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
    @PreAuthorize("hasPermission(#productBase.orgId, 'filterByOrg', 'productbase:create')")
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
        productBaseDomain.saveProductBaseDetails(productBase.getDetails(), productBaseObject.getOrgId(), id, productBaseObject.getVersion());
        return new ProductBase(newProductBaseObject);
    }

    //create new product base versions
    @RequestMapping(value = "{product_base_id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#productBase.orgId, 'filterByOrg', 'productbase:modify')")
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
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
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
            productBaseObject.setOrgId(tokenAuthenticationService.getAuthentication().getDetails().getOrgId());
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

        ProductBaseVersionsObject newProductBaseVersionsObject = productBaseDomain.createProductBaseVersions(productBaseVersionsObject);
        productBaseDomain.saveProductBaseDetails(productBase.getDetails(), productBaseObject.getOrgId(), productBaseId, newProductBaseVersionsObject.getVersion());
        return newProductBaseVersionsObject;
    }

    //update product base versions: edit current product version detail
    @RequestMapping(value = "{product_base_id}", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#productBase.orgId, 'filterByOrg', 'productbase:modify')")
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
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        if (StringUtils.hasText(productBase.getOrgId()))
            productBaseObject.setOrgId(productBase.getOrgId());
        else
            productBaseObject.setOrgId(tokenAuthenticationService.getAuthentication().getDetails().getOrgId());

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
        productBaseDomain.patchUpdate(productBaseVersionsObject);
        productBaseDomain.saveProductBaseDetails(productBase.getDetails(), productBaseObject.getOrgId(), productBaseId, version);
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
        if (LookupCodes.ProductBaseVersionsApprovalStatus.APPROVED.equals(approvalStatus)) {
            productBaseVersionsObject.setStatusCode(LookupCodes.ProductBaseVersionsStatus.ACTIVATED);
            if (reviewComments != null) {
                productBaseVersionsObject.setReviewComments(reviewComments);
            }
            productBaseDomain.patchUpdate(productBaseVersionsObject);
            ProductBaseObject productBaseObject = productBaseDomain.copyFromProductBaseVersionsObject(productBaseVersionsObject);
            productBaseDomain.updateProductBase(productBaseObject);
        }
        if (LookupCodes.ProductBaseVersionsApprovalStatus.REJECTED.equals(approvalStatus)) {
            productBaseVersionsObject.setStatusCode(LookupCodes.ProductBaseVersionsStatus.REJECTED);
            if (reviewComments != null) {
                productBaseVersionsObject.setReviewComments(reviewComments);
            }
            productBaseDomain.patchUpdate(productBaseVersionsObject);
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
                TPermission tPermission = new TPermission(productBaseObject.getOrgId(), "productbase", "delete");
                if (!accountPermissionDomain.hasPermission(tokenAuthenticationService.getAuthentication().getDetails().getId(), tPermission)) {
                    throw new ForbiddenException("operation denied");
                }
                productBaseDomain.deleteProductBase(productBaseId);
            } else {
                TPermission tPermission = new TPermission(productBaseObject.getOrgId(), "productbase", "modify");
                if (!accountPermissionDomain.hasPermission(tokenAuthenticationService.getAuthentication().getDetails().getId(), tPermission)) {
                    throw new ForbiddenException("operation denied");
                }
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

    /**
     * @param productBaseId id of product base
     * @param version       it will be current active version of product base if it's null
     * @param imageRequest  image base64 string format with schema header and crop arguments
     */
    @RequestMapping(value = "{product_base_id}/image", method = RequestMethod.PUT)
    public void putProductBaseImage(@PathVariable(value = "product_base_id") String productBaseId,
                                    @RequestParam(value = "version", required = false) Integer version,
                                    @RequestBody @Valid ImageRequest imageRequest) {
        ProductBaseObject productBaseObject = findProductBaseById(productBaseId);
        String orgId = productBaseObject.getOrgId();
        Integer currentVersion = productBaseObject.getVersion();
        if (version == null) {
            version = currentVersion;
        }
        //check permission
        //todo:

        //find the edit version
        ProductBaseVersionsObject productBaseVersionsObject = productBaseDomain.getProductBaseVersionsByProductBaseIdAndVersion(productBaseId, version);
        if (productBaseVersionsObject == null || !LookupCodes.ProductBaseVersionsStatus.EDITABLE_STATUS.contains(productBaseVersionsObject.getStatusCode())) {
            throw new UnprocessableEntityException("image can only be uploaded to the editable product base");
        }

        productBaseDomain.saveProductBaseImage(imageRequest, orgId, productBaseId, version);
        LOGGER.info("image saved [orgId: {}, productBaseId:{}, version:{}]", orgId, productBaseId, version);
    }

    /**
     * @param productBaseId id of product base
     * @param imageName     Ex. image-800x400
     * @param version       it will be current active version of product base if it's null
     * @return image
     */
    @RequestMapping(value = "{product_base_id}/image/{image_name}", method = RequestMethod.GET)
    public ResponseEntity<?> getProductBaseImage(
            @PathVariable(value = "product_base_id") String productBaseId,
            @PathVariable(value = "image_name") String imageName,
            @RequestParam(value = "version", required = false) Integer version) {
        ProductBaseObject productBaseObject = findProductBaseById(productBaseId);
        String orgId = productBaseObject.getOrgId();
        Integer currentVersion = productBaseObject.getVersion();
        if (version == null) {
            version = currentVersion;
        }

        ResourceInputStream resourceInputStream = productBaseDomain.getProductBaseImage(orgId, productBaseId, version, imageName);
        if (resourceInputStream == null) {
            throw new NotFoundException("image not found");
        }
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
        if (resourceInputStream.getContentLength() > 0) {
            builder.contentLength(resourceInputStream.getContentLength());
        }
        return builder.body(new InputStreamResource(resourceInputStream));
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

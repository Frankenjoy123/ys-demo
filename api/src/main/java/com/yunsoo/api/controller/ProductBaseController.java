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
import java.util.ArrayList;
import java.util.Iterator;
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

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'productbase:read')")
    public ProductBase getById(@PathVariable(value = "id") String id) throws IOException {
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(id);
        if (productBaseObject == null) {
            throw new NotFoundException("product base not found");
        }
        ProductBase productBase = new ProductBase(productBaseObject);
        productBase.setCategory(new ProductCategory(productCategoryDomain.getById(productBase.getCategoryId())));
        productBase.setProductKeyTypes(LookupObject.fromCodeList(lookupDomain.getProductKeyTypes(), productBaseObject.getProductKeyTypeCodes()));
        productBase.setProductBaseDetails(productBaseDomain.getProductBaseDetailByProductBaseIdAndVersion(id, productBaseObject.getOrgId(), productBaseObject.getVersion()));
        return productBase;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'productbase:read')")
    public List<ProductBase> getByOrgId(@RequestParam(value = "org_id", required = false) String orgId,
                                        Pageable pageable,
                                        HttpServletResponse response) {
        orgId = fixOrgId(orgId);
        Map<String, ProductCategoryObject> productCategoryObjectMap = productCategoryDomain.getProductCategoryMap();
        List<ProductKeyType> productKeyTypes = lookupDomain.getProductKeyTypes();
        Page<ProductBaseObject> productBasePage = productBaseDomain.getProductBaseByOrgId(orgId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", productBasePage.toContentRange());
        }
        List<ProductBase> productBases = productBasePage.map(p -> {
            ProductBase pb = new ProductBase(p);
            pb.setCategory(new ProductCategory(productCategoryDomain.getById(p.getCategoryId(), productCategoryObjectMap)));
            pb.setProductKeyTypes(LookupObject.fromCodeList(productKeyTypes, p.getProductKeyTypeCodes()));
            return pb;
        }).getContent();

        List<String> productBaseIds = productBases.stream().map(ProductBase::getId).collect(Collectors.toList());
        Map<String, List<ProductBaseVersionsObject>> map = productBaseDomain.getProductBaseVersionsByProductBaseIds(productBaseIds);
        for (ProductBase pb : productBases) {
            List<ProductBaseVersionsObject> productBaseVersionsObjects = map.get(pb.getId());
            if (productBaseVersionsObjects != null) {
                List<ProductBaseVersions> productBaseVersions = new ArrayList<ProductBaseVersions>();
                Iterator<ProductBaseVersionsObject> iter = productBaseVersionsObjects.iterator();
                while (iter.hasNext()) {
                    productBaseVersions.add(new ProductBaseVersions(iter.next()));
                }
                pb.setProductBaseVersions(productBaseVersions);
            }
        }
        return productBases;
    }

    @RequestMapping(value = "productbaseversions", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'productbase:read')")
    public List<ProductBase> getProductBaseVersionsByOrgId(@RequestParam(value = "org_id", required = false) String orgId,
                                                           Pageable pageable,
                                                           HttpServletResponse response) {
        List<ProductBase> productBases = new ArrayList<ProductBase>();
        orgId = fixOrgId(orgId);
        Page<ProductBaseObject> productBasePage = productBaseDomain.getProductBaseByOrgId(orgId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", productBasePage.toContentRange());
        }
        List<String> productBaseIds = productBasePage.map(ProductBaseObject::getId).getContent();
        Map<String, List<ProductBaseVersionsObject>> map = productBaseDomain.getProductBaseVersionsByProductBaseIds(productBaseIds);
        Iterator it = map.values().iterator();
        while (it.hasNext()) {
            List<ProductBaseVersionsObject> productBaseVersionsObjects = (List<ProductBaseVersionsObject>) it.next();
            Iterator<ProductBaseVersionsObject> iter = productBaseVersionsObjects.iterator();
            while (iter.hasNext()) {
                ProductBase productBase = new ProductBase(iter.next().getProductBase());
                productBases.add(productBase);
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
        productBaseDomain.createProductBaseFile(productBase, id, productBaseObject.getOrgId(), productBaseObject.getVersion());
        return new ProductBase(newProductBaseObject);
    }

    //update product base versions: created new product version or edit current product version detail
    @RequestMapping(value = "{product_base_id}", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#productBase.orgId, 'filterByOrg', 'productbase:modify')")
    public void updateProductBaseVersions(@PathVariable(value = "product_base_id") String productBaseId,
                                          @RequestBody ProductBase productBase) {
        ProductBaseObject productBaseObject = new ProductBaseObject();
        ProductBaseVersionsObject productBaseVersionsObject = new ProductBaseVersionsObject();
        List<ProductBaseVersionsObject> productBaseVersionsObjects = productBaseDomain.getProductBaseVersionsByProductBaseId(productBaseId);
        if (productBaseVersionsObjects.size() == 0) {
            throw new NotFoundException("product base version not found");
        }

        String currentVersionStatus = productBaseVersionsObjects.get(productBaseVersionsObjects.size() - 1).getStatusCode();
        Integer currentVersion = productBaseVersionsObjects.get(productBaseVersionsObjects.size() - 1).getVersion();
        Integer actualVersion = currentVersion;
        if (LookupCodes.ProductBaseVersionsStatus.SUBMITTED.equals(currentVersionStatus)) {
            throw new UnprocessableEntityException("Sorry, you can't process current product version!");
        }
        productBaseObject.setId(productBaseId);
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
        productBaseVersionsObject.setVersion(currentVersion);
        productBaseVersionsObject.setStatusCode(LookupCodes.ProductBaseVersionsStatus.SUBMITTED);
        productBaseVersionsObject.setCreatedAccountId(currentAccountId);
        productBaseVersionsObject.setCreatedDateTime(DateTime.now());

        if (LookupCodes.ProductBaseVersionsStatus.ACTIVATED.equals(currentVersionStatus)) {
            productBaseDomain.createProductBaseVersions(productBaseVersionsObject);
            actualVersion = currentVersion + 1;
        }
        if (LookupCodes.ProductBaseVersionsStatus.DRAFT.equals(currentVersionStatus) || LookupCodes.ProductBaseVersionsStatus.REJECTED.equals(currentVersionStatus)) {
            productBaseVersionsObject.setModifiedAccountId(currentAccountId);
            productBaseVersionsObject.setModifiedDateTime(DateTime.now());
            productBaseDomain.patchUpdate(productBaseVersionsObject);
        }
        productBaseDomain.createProductBaseFile(productBase, productBaseId, productBaseObject.getOrgId(), actualVersion);
    }

    //delete product base
    @RequestMapping(value = "{product_base_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "product_base_id") String productBaseId) {
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(productBaseId);
        if (productBaseObject != null) {
            TPermission tPermission = new TPermission(productBaseObject.getOrgId(), "productbase", "delete");
            if (!accountPermissionDomain.hasPermission(tokenAuthenticationService.getAuthentication().getDetails().getId(), tPermission)) {
                throw new ForbiddenException();
            }
            productBaseDomain.deleteProductBase(productBaseId);
        }
    }

    //delete product base versions
    @RequestMapping(value = "{product_base_id}/{version}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductBaseVersions(@PathVariable(value = "product_base_id") String productBaseId,
                                          @PathVariable(value = "version") Integer version) {
        productBaseDomain.deleteProductBaseVersions(productBaseId, version);
    }

    //endregion


    //region product base image

    @RequestMapping(value = "{product_base_id}/image", method = RequestMethod.PUT)
    public void putProductBaseImage(@PathVariable(value = "product_base_id") String productBaseId,
                                    @RequestParam(value = "version", required = false) Integer version,
                                    @RequestBody @Valid ImageRequest imageRequest) {
        ProductBaseObject productBaseObject = findProductBaseById(productBaseId);
        String orgId = productBaseObject.getOrgId();
        Integer currentVersion = productBaseObject.getVersion();
        if (version == null || version < 1 || version > currentVersion + 1) {
            version = currentVersion + 1;
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

    @RequestMapping(value = "{product_base_id}/image/{image_name}", method = RequestMethod.GET)
    public ResponseEntity<?> getProductBaseImage(
            @PathVariable(value = "product_base_id") String productBaseId,
            @PathVariable(value = "image_name") String imageName,
            @RequestParam(value = "version", required = false) Integer version) {
        ProductBaseObject productBaseObject = findProductBaseById(productBaseId);
        String orgId = productBaseObject.getOrgId();
        Integer currentVersion = productBaseObject.getVersion();
        if (version == null || version < 1 || version > currentVersion + 1) {
            version = currentVersion;
        }

        ResourceInputStream resourceInputStream = productBaseDomain.getProductBaseImage(productBaseId, orgId, version, imageName);
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

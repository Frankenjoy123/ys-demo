package com.yunsoo.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.api.domain.AccountPermissionDomain;
import com.yunsoo.api.domain.LookupDomain;
import com.yunsoo.api.domain.ProductBaseDomain;
import com.yunsoo.api.domain.ProductCategoryDomain;
import com.yunsoo.api.dto.*;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private RestClient dataAPIClient;

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

    //create image

    @RequestMapping(value = "{product_base_id}/{version}/image/{imgdetail}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createImage(MultipartHttpServletRequest request, HttpServletResponse response,
                            @PathVariable(value = "product_base_id") String productBaseId,
                            @PathVariable(value = "version") Integer version,
                            @PathVariable(value = "imgdetail") String imgDetail) {
        try {
            Iterator<String> itr = request.getFileNames();
            MultipartFile file = request.getFile(itr.next());

            FileObject fileObject = new FileObject();
            fileObject.setData(file.getBytes());
            fileObject.setLength(file.getSize());
            fileObject.setContentType(file.getContentType());
            fileObject.setS3Path("photo/coms/products/" + productBaseId + "/" + version.toString() + "/" + imgDetail);

            dataAPIClient.post("file/", fileObject, Long.class);

        } catch (IOException ex) {
            throw new InternalServerErrorException("图片上传出错！");
        }
    }

    //create product base image

    @RequestMapping(value = "{product_base_id}/image", method = RequestMethod.PUT)
    public void createProductBaseImage(@RequestBody ProductBaseImage productBaseImage) {

        String productBaseId = productBaseImage.getProductBaseId();
        List<ProductBaseVersionsObject> productBaseVersionsObjects = productBaseDomain.getProductBaseVersionsByProductBaseId(productBaseId);
        if (productBaseVersionsObjects.size() == 0) {
            throw new NotFoundException("product base version not found");
        }
        String currentVersionStatus = productBaseVersionsObjects.get(productBaseVersionsObjects.size() - 1).getStatusCode();
        Integer currentVersion = productBaseVersionsObjects.get(productBaseVersionsObjects.size() - 1).getVersion();
        String orgId = productBaseVersionsObjects.get(productBaseVersionsObjects.size() - 1).getProductBase().getOrgId();

        if (LookupCodes.ProductBaseVersionsStatus.ACTIVATED.equals(currentVersionStatus)) {
            currentVersion = currentVersion + 1;
        }
        productBaseDomain.createProductBaseImage(productBaseImage, orgId, currentVersion);
    }


    //query for image
    @RequestMapping(value = "{product_base_id}/image", method = RequestMethod.GET)
    public ResponseEntity<?> getImage(
            @PathVariable(value = "product_base_id") String productBaseId,
            @RequestParam(value = "version", required = false) Integer version,
            @RequestParam(value = "imgdetail") String imgDetail) {
        Integer currenteVersion = new Integer(version);
        if (version == null) {
            ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(productBaseId);
            if (productBaseObject == null) {
                throw new NotFoundException("product base not found");
            }
            currenteVersion = productBaseObject.getVersion();
        }
        try {
            FileObject fileObject = dataAPIClient.get("productbaseversions/{product_base_id}/{version}/image/{imgdetail}", FileObject.class, productBaseId, currenteVersion.toString(), imgDetail);
            if (fileObject.getLength() > 0) {
                return ResponseEntity.ok()
                        .contentLength(fileObject.getLength())
                        .contentType(MediaType.parseMediaType(fileObject.getContentType()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(fileObject.getContentType()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
            }
        } catch (NotFoundException ex) {
            throw new NotFoundException(40402, "找不到产品图片 product_base_id = " + productBaseId + "  version = " + version.toString());
        }
    }

    //create product base
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#productBase.orgId, 'filterByOrg', 'productbase:create')")
    public ProductBase create(@RequestBody ProductBase productBase) {
        ProductBaseObject productBaseObject = new ProductBaseObject();
        ProductBaseVersionsObject productBaseVersionsObject = new ProductBaseVersionsObject();
        productBaseObject.setName(productBase.getName());
        productBaseObject.setVersion(LookupCodes.ProductBaseVersions.INITIALVERSION);
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();

        if (StringUtils.hasText(productBase.getOrgId()))
            productBaseObject.setOrgId(productBase.getOrgId());
        else
            productBaseObject.setOrgId(tokenAuthenticationService.getAuthentication().getDetails().getOrgId());

        productBaseObject.setBarcode(productBase.getBarcode());
        productBaseObject.setVersion(LookupCodes.ProductBaseVersions.INITIALVERSION);
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
        productBaseVersionsObject.setVersion(LookupCodes.ProductBaseVersions.INITIALVERSION);
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


    //create with details
    @RequestMapping(value = "/withdetail", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductBase createDetails(@RequestBody ProductBaseRequest productBase) {

        ProductBaseObject p = new ProductBaseObject();
        p.setName(productBase.getName());

        if (StringUtils.hasText(productBase.getOrgId()))
            p.setOrgId(productBase.getOrgId());
        else
            p.setOrgId(tokenAuthenticationService.getAuthentication().getDetails().getOrgId());

        p.setBarcode(productBase.getBarcode());
        p.setStatusCode(productBase.getStatusCode());
        p.setCategoryId(productBase.getCategoryId());
        p.setChildProductCount(productBase.getChildProductCount());
        p.setComments(productBase.getComments());
        p.setCreatedDateTime(productBase.getCreatedDateTime());
        p.setId(productBase.getId());
        p.setModifiedDateTime(productBase.getModifiedDateTime());
        p.setProductKeyTypeCodes(productBase.getProductKeyTypeCodes());
        p.setShelfLife(productBase.getShelfLife());
        p.setShelfLifeInterval(productBase.getShelfLifeInterval());

        String id = dataAPIClient.post("productbase/", p, String.class);

        if (StringUtils.hasText(id)) {
            FileObject fileObject = new FileObject();
            fileObject.setData(productBase.getDetails().getBytes(StandardCharsets.UTF_8));
            fileObject.setContentType("application/octet-stream");
            fileObject.setS3Path("photo/coms/products/" + id + "/notes.json");

            dataAPIClient.post("file/", fileObject, Long.class);
        }

        ProductBase productBase1 = new ProductBase();
        productBase1.setId(id);

        return productBase1;
    }

    @RequestMapping(value = "withdetailfile/{id}/{filekey}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createDetailsThumbnail(MultipartHttpServletRequest request, HttpServletResponse response,
                                       @PathVariable(value = "id") String id,
                                       @PathVariable(value = "filekey") String filekey) {
        try {
            Iterator<String> itr = request.getFileNames();
            MultipartFile file = request.getFile(itr.next());

            FileObject fileObject = new FileObject();
            fileObject.setData(file.getBytes());
            fileObject.setLength(file.getSize());
            fileObject.setContentType(file.getContentType());
            fileObject.setS3Path("photo/coms/products/" + id + "/" + filekey);

            dataAPIClient.post("file/", fileObject, Long.class);

        } catch (IOException ex) {
            throw new InternalServerErrorException("文件上传出错！");
        }
    }

    //delete product base
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") String id) {
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(id);
        if (productBaseObject != null) {
            TPermission tPermission = new TPermission(productBaseObject.getOrgId(), "productbase", "delete");
            if (!accountPermissionDomain.hasPermission(tokenAuthenticationService.getAuthentication().getDetails().getId(), tPermission)) {
                throw new ForbiddenException();
            }
            productBaseDomain.deleteProductBase(id);
        }
    }

    //delete product base versions
    @RequestMapping(value = "{product_base_id}/{version}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductBaseVersions(@PathVariable(value = "product_base_id") String productBaseId,
                                          @PathVariable(value = "version") Integer version) {
        productBaseDomain.deleteProductBaseVersions(productBaseId, version);
    }


    @RequestMapping(value = "/{id}/{client}", method = RequestMethod.GET)
    public ResponseEntity<?> getThumbnail(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "client") String client) {
        try {
            FileObject fileObject = dataAPIClient.get("productbase/{id}/{client}", FileObject.class, id, client);
            if (fileObject.getLength() > 0) {
                return ResponseEntity.ok()
                        .contentLength(fileObject.getLength())
                        .contentType(MediaType.parseMediaType(fileObject.getContentType()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(fileObject.getContentType()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
            }
        } catch (NotFoundException ex) {
            throw new NotFoundException(40402, "找不到产品图片 id = " + id + "  client = " + client);
        }
    }

    @RequestMapping(value = "/{id}/{filekey}", method = RequestMethod.POST)
    public void UpdateThumbnail(MultipartHttpServletRequest request, HttpServletResponse response,
                                @PathVariable(value = "id") String id,
                                @PathVariable(value = "filekey") String filekey) {
        try {
            Iterator<String> itr = request.getFileNames();
            MultipartFile file = request.getFile(itr.next());

            FileObject fileObject = new FileObject();
            fileObject.setData(file.getBytes());
            fileObject.setLength(file.getSize());
            fileObject.setContentType(file.getContentType());

            dataAPIClient.post("productbase/{id}/{filekey}", fileObject, id, filekey);
        } catch (IOException ex) {
            throw new InternalServerErrorException("文件获取出错！");
        }
    }

    private String fixOrgId(String orgId) {
        if (orgId == null || "current".equals(orgId)) {
            //current orgId
            return tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }
        return orgId;
    }
}

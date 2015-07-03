package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountPermissionDomain;
import com.yunsoo.api.domain.ProductBaseDomain;
import com.yunsoo.api.dto.ProductBase;
import com.yunsoo.api.dto.ProductBaseRequest;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
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
import java.util.Iterator;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/20
 * Descriptions:
 * <p>
 * * ErrorCode
 * 40401    :   产品找不到
 * 40402    :   产品Thumbnail找不到
 */
@RestController
@RequestMapping(value = "/productbase")
public class ProductBaseController {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private ProductBaseDomain productBaseDomain;

    @Autowired
    private AccountPermissionDomain accountPermissionDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'productbase:read')")
    public ProductBase get(@PathVariable(value = "id") String id) {
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("ProductBaseId不应为空！");
        }
        ProductBase productBase = productBaseDomain.getProductBaseById(id);
        if (productBase == null) {
            throw new NotFoundException(40401, "找不到产品");
        }
        return productBase;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'productbase:read')")
    public List<ProductBase> getAllForCurrentOrg(@RequestParam(value = "org_id", required = false) String orgId) {
        if (orgId == null || orgId.isEmpty()) {
            orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId(); //fetch from AuthContext
        }
        return productBaseDomain.getAllProductBaseByOrgId(orgId);
    }

    //create
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#productBase.orgId, 'filterByOrg', 'productbase:create')")
    public String create(@RequestBody ProductBase productBase) {
        ProductBaseObject p = new ProductBaseObject();
        //BeanUtils.copyProperties(productBase, p);

        p.setName(productBase.getName());

        if (StringUtils.hasText(productBase.getOrgId()))
            p.setOrgId(productBase.getOrgId());
        else
            p.setOrgId(tokenAuthenticationService.getAuthentication().getDetails().getOrgId());

        p.setBarcode(productBase.getBarcode());
        p.setStatus(productBase.getStatusCode());
        p.setCategoryId(productBase.getCategoryId());
        p.setChildProductCount(productBase.getChildProductCount());
        p.setComment(productBase.getComments());
        p.setCreatedDateTime(productBase.getCreatedDateTime());
        p.setId(productBase.getId());
        p.setModifiedDateTime(productBase.getModifiedDateTime());
        p.setProductKeyTypeCodes(productBase.getProductKeyTypeCodes());
        p.setShelfLife(productBase.getShelfLife());
        p.setShelfLifeInterval(productBase.getShelfLifeInterval());

        String id = dataAPIClient.post("productbase/", p, String.class);
        return id;
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
        p.setStatus(productBase.getStatusCode());
        p.setCategoryId(productBase.getCategoryId());
        p.setChildProductCount(productBase.getChildProductCount());
        p.setComment(productBase.getComments());
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

    //patch update
    @RequestMapping(value = "", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#productBase, 'productbase:modify')")
    public void update(@RequestBody ProductBase productBase) throws Exception {
        //patch update, we don't provide functions like update with set null properties.
        ProductBaseObject p = new ProductBaseObject();
        BeanUtils.copyProperties(productBase, p);
        dataAPIClient.patch("productbase/", p);
    }

    //delete
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") String id) {
        ProductBase productBase = productBaseDomain.getProductBaseById(id);
        if (productBase == null) {
            return;  //when the product base is not exist!
        }

        TPermission tPermission = new TPermission();
        tPermission.setOrgId(productBase.getOrgId());
        tPermission.setResourceCode("Productbase");
        tPermission.setActionCode("delete");
        if (!accountPermissionDomain.hasPermission(tokenAuthenticationService.getAuthentication().getDetails().getId(), tPermission)) {
            throw new ForbiddenException("没有权限删此产品记录！");
        }
        productBase.setStatusCode(LookupCodes.ProductBaseStatus.DELETED);  //just mark as inactive
        ProductBaseObject p = new ProductBaseObject();
        BeanUtils.copyProperties(productBase, p);
        dataAPIClient.patch("productbase/", p);
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

}

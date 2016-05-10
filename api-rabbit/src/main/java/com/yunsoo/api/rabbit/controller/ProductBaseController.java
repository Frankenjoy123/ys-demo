package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.FileDomain;
import com.yunsoo.api.rabbit.domain.ProductBaseDomain;
import com.yunsoo.api.rabbit.domain.ProductDomain;
import com.yunsoo.api.rabbit.domain.UserFollowDomain;
import com.yunsoo.api.rabbit.dto.ProductBase;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2015/3/20
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/productbase")
public class ProductBaseController {

    @Autowired
    private ProductDomain productDomain;

    @Autowired
    private ProductBaseDomain productBaseDomain;

    @Autowired
    private UserFollowDomain followDomain;

    @Autowired
    private FileDomain fileDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductBase getProductBaseById(@PathVariable(value = "id") String id) throws Exception {
        String userId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(id);
        if (productBaseObject == null) {
            throw new NotFoundException("product base not found by [id: " + id + "]");
        }
        ProductBase productBase = new ProductBase(productBaseObject);
        productBase.setCategory(productBaseDomain.getProductCategoryById(productBaseObject.getCategoryId()));

        //set detail info
        productBase.setCommentsScore(productDomain.getCommentsScore(id));
        //set pageable to null
        // productBase.setFollowingUsers(productDomain.getFollowingUsersByProductBaseId(id, null).getContent());
        productBase.setIsFollowing(followDomain.getUserProductFollowingByUserIdAndProductBaseId(userId, id) != null);
        return productBase;
    }

    @RequestMapping(value = "{product_base_id}/image/{image_name}", method = RequestMethod.GET)
    public ResponseEntity<?> getProductBaseImage(
            @PathVariable(value = "product_base_id") String productBaseId,
            @PathVariable(value = "image_name") String imageName) {
        ResourceInputStream resourceInputStream = productBaseDomain.getProductBaseImage(productBaseId, imageName);
        if (resourceInputStream == null) {
            throw new NotFoundException("product image found");
        }
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
        if (resourceInputStream.getContentLength() > 0) {
            builder.contentLength(resourceInputStream.getContentLength());
        }
        return builder.body(new InputStreamResource(resourceInputStream));

    }

    @RequestMapping(value = "{product_base_id}/details", method = RequestMethod.GET)
    public ResponseEntity<?> getProductBaseDetails(@PathVariable(value = "product_base_id") String productBaseId) {
        ProductBaseObject productBaseObject = findProductBaseById(productBaseId);
        ResourceInputStream resourceInputStream = getProductBaseFile(productBaseObject, "details.json");
        if (resourceInputStream == null) {
            throw new NotFoundException("product base details not found");
        }
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        bodyBuilder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
        bodyBuilder.contentLength(resourceInputStream.getContentLength());
        return bodyBuilder.body(new InputStreamResource(resourceInputStream));
    }

    @RequestMapping(value = "{product_base_id}/adminPageInfo", method = RequestMethod.GET)
    public ResponseEntity<?> getProductBaseAdminPageInfo(@PathVariable(value = "product_base_id") String productBaseId) {
        ProductBaseObject productBaseObject = findProductBaseById(productBaseId);
        ResourceInputStream resourceInputStream = getProductBaseFile(productBaseObject, "admin_page_info.json");
        if (resourceInputStream == null) {
            throw new NotFoundException("product base details not found");
        }
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        bodyBuilder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
        bodyBuilder.contentLength(resourceInputStream.getContentLength());
        return bodyBuilder.body(new InputStreamResource(resourceInputStream));
    }

    private ProductBaseObject findProductBaseById(String id) {
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(id);
        if (productBaseObject == null) {
            throw new NotFoundException("product base not found by [id: " + id + "]");
        }
        return productBaseObject;
    }

    private ResourceInputStream getProductBaseFile(ProductBaseObject productBaseObject, String relativePath) {
        String path = String.format("organization/%s/product_base/%s/%s/%s",
                productBaseObject.getOrgId(), productBaseObject.getId(), productBaseObject.getVersion(), relativePath);
        ResourceInputStream resourceInputStream = fileDomain.getFile(path);
        if (resourceInputStream == null) {
            throw new NotFoundException();
        }
        return resourceInputStream;
    }
}

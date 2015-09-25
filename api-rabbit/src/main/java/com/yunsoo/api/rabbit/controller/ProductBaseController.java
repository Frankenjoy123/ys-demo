package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.ProductBaseDomain;
import com.yunsoo.api.rabbit.domain.ProductDomain;
import com.yunsoo.api.rabbit.dto.Organization;
import com.yunsoo.api.rabbit.dto.ProductBase;
import com.yunsoo.api.rabbit.dto.ProductBaseDetails;
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

import java.util.List;

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

    @RequestMapping( method = RequestMethod.GET)
    public List<ProductBase> getByFilter(){
        return productBaseDomain.getAllProductBases();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductBase getProductBaseById(@PathVariable(value = "id") String id) throws Exception{
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(id);
        if (productBaseObject == null) {
            throw new NotFoundException("product base not found.");
        }
        ProductBase productBase = new ProductBase(productBaseObject);
        productBase.setCategory(productBaseDomain.getProductCategoryById(productBaseObject.getCategoryId()));

        //set details
        productBase.setDetails(productBaseDomain.getProductBaseDetailsById(id));
        //set detail info
        productBase.setCommentsScore(productDomain.getCommentsScore(id));
        //set pageable to null
        productBase.setFollowingUsers(productDomain.getFollowingUsersByProductBaseId(id, null).getContent());

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
    public ProductBaseDetails getProductBaseDetails(
            @PathVariable(value = "product_base_id") String productBaseId) {
        ProductBaseDetails details = productBaseDomain.getProductBaseDetailsById(productBaseId);
        if (details == null) {
            throw new NotFoundException("找不到产品详细信息");
        }

        return  details;
    }
}

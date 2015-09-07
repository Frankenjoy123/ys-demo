package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.ProductDomain;
import com.yunsoo.api.rabbit.dto.ProductBase;
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

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductBase getById(@PathVariable(value = "id") String id) {
        ProductBase productBase = productDomain.getProductBaseById(id);
        if (productBase == null) {
            throw new NotFoundException("找不到产品");
        }
        return productBase;
    }

    @RequestMapping(value = "{product_base_id}/image/{image_name}", method = RequestMethod.GET)
    public ResponseEntity<?> getProductBaseImage(
            @PathVariable(value = "product_base_id") String productBaseId,
            @PathVariable(value = "image_name") String imageName) {
        ResourceInputStream resourceInputStream = productDomain.getProductBaseImage(productBaseId, imageName);
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
    public ResponseEntity<?> getProductBaseDetails(
            @PathVariable(value = "product_base_id") String productBaseId) {
        ResourceInputStream resourceInputStream = productDomain.getProductBaseDetailsById(productBaseId);
        if (resourceInputStream == null) {
            throw new NotFoundException("product details not found");
        }
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
        if (resourceInputStream.getContentLength() > 0) {
            builder.contentLength(resourceInputStream.getContentLength());
        }
        return builder.body(new InputStreamResource(resourceInputStream));
    }
}

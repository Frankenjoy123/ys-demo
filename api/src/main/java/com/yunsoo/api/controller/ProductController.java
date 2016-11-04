package com.yunsoo.api.controller;

import com.yunsoo.api.key.Constants;
import com.yunsoo.api.key.dto.Product;
import com.yunsoo.api.key.service.ProductService;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by:   Lijian
 * Created on:   2015/3/9
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public Product get(@PathVariable(value = "key") String key) {
        return findProduct(key);
    }

    @RequestMapping(value = "{key}/active", method = RequestMethod.POST)
    public void active(@PathVariable(value = "key") String key) {
        productService.setProductStatusByKey(key, Constants.ProductStatus.ACTIVATED);
    }

    @RequestMapping(value = "{key}/recall", method = RequestMethod.POST)
    public void recall(@PathVariable(value = "key") String key) {
        productService.setProductStatusByKey(key, Constants.ProductStatus.RECALLED);
    }

    @RequestMapping(value = "{key}/delete", method = RequestMethod.POST)
    public void delete(@PathVariable(value = "key") String key) {
        productService.setProductStatusByKey(key, Constants.ProductStatus.DELETED);
    }

    @RequestMapping(value = "external/{partitionId}/{externalKey}/active", method = RequestMethod.POST)
    public void activeByExternalKey(@PathVariable(value = "partitionId") String partitionId,
                                    @PathVariable(value = "externalKey") String externalKey) {
        productService.setProductStatusByExternalKey(partitionId, externalKey, Constants.ProductStatus.ACTIVATED);
    }

    @RequestMapping(value = "external/{partitionId}/{externalKey}/recall", method = RequestMethod.POST)
    public void recallByExternalKey(@PathVariable(value = "partitionId") String partitionId,
                                    @PathVariable(value = "externalKey") String externalKey) {
        productService.setProductStatusByExternalKey(partitionId, externalKey, Constants.ProductStatus.RECALLED);
    }

    @RequestMapping(value = "external/{partitionId}/{externalKey}/delete", method = RequestMethod.POST)
    public void deleteByExternalKey(@PathVariable(value = "partitionId") String partitionId,
                                    @PathVariable(value = "externalKey") String externalKey) {
        productService.setProductStatusByExternalKey(partitionId, externalKey, Constants.ProductStatus.DELETED);
    }

    @RequestMapping(value = "{key}/details", method = RequestMethod.GET)
    public ResponseEntity<?> getDetails(@PathVariable(value = "key") String key) {
        String details = findProduct(key).getDetails();
        byte[] buffer = details == null ? new byte[0] : details.getBytes(StandardCharsets.UTF_8);
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        bodyBuilder.contentType(MediaType.APPLICATION_JSON_UTF8);
        bodyBuilder.contentLength(buffer.length);
        return bodyBuilder.body(new InputStreamResource(new ByteArrayInputStream(buffer)));
    }

    @RequestMapping(value = "{key}/details", method = RequestMethod.PUT)
    public void putDetails(@PathVariable(value = "key") String key,
                           @RequestBody String details) {
        Product product = new Product();
        product.setKey(key);
        product.setDetails(details);
        productService.patchUpdate(product);
    }

    private Product findProduct(String key) {
        Product product = productService.getProductByKey(key);
        if (product == null) {
            throw new NotFoundException("product not found by key " + key);
        }
        return product;
    }

}


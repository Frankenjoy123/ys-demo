package com.yunsoo.api.controller;

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

    @RequestMapping(value = "/{key}/active", method = RequestMethod.POST)
    public void active(@PathVariable(value = "key") String key) {
        productService.activeProduct(key);
    }

    @RequestMapping(value = "/{key}/delete", method = RequestMethod.POST)
    public void delete(@PathVariable(value = "key") String key) {
        productService.deleteProduct(key);
    }

    @RequestMapping(value = "/{key}/details", method = RequestMethod.GET)
    public ResponseEntity<?> getDetails(@PathVariable(value = "key") String key) {
        String details = findProduct(key).getDetails();
        byte[] buffer = details == null ? new byte[0] : details.getBytes(StandardCharsets.UTF_8);
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        bodyBuilder.contentType(MediaType.APPLICATION_JSON_UTF8);
        bodyBuilder.contentLength(buffer.length);
        return bodyBuilder.body(new InputStreamResource(new ByteArrayInputStream(buffer)));
    }

    @RequestMapping(value = "/{key}/details", method = RequestMethod.PUT)
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


package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductDomain;
import com.yunsoo.api.domain.ProductKeyDomain;
import com.yunsoo.api.dto.Product;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ProductObject;
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
    private ProductDomain productDomain;

    @Autowired
    private ProductKeyDomain productKeyDomain;


    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public Product get(@PathVariable(value = "key") String key) {
        ProductObject productObject = findProduct(key);
        Product product = new Product(productObject);
        ProductKeyBatchObject productKeyBatchObject = productKeyDomain.getProductKeyBatchObjectById(productObject.getProductKeyBatchId());
        if (productKeyBatchObject != null) {
            product.setOrgId(productKeyBatchObject.getOrgId());
        }
        return product;
    }

    @RequestMapping(value = "/{key}/active", method = RequestMethod.POST)
    public void active(@PathVariable(value = "key") String key) {
        productDomain.activeProduct(key);
    }

    @RequestMapping(value = "/{key}/delete", method = RequestMethod.POST)
    public void delete(@PathVariable(value = "key") String key) {
        productDomain.deleteProduct(key);
    }

    @RequestMapping(value = "/{key}/details", method = RequestMethod.GET)
    public ResponseEntity<?> getDetails(@PathVariable(value = "key") String key) {
        String details = findProduct(key).getDetails();
        byte[] buffer = details == null ? new byte[0] : details.getBytes(StandardCharsets.UTF_8);
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        bodyBuilder.contentType(MediaType.APPLICATION_JSON);
        bodyBuilder.contentLength(buffer.length);
        return bodyBuilder.body(new InputStreamResource(new ByteArrayInputStream(buffer)));
    }

    @RequestMapping(value = "/{key}/details", method = RequestMethod.PUT)
    public void putDetails(@PathVariable(value = "key") String key,
                           @RequestBody String details) {
        ProductObject productObject = new ProductObject();
        productObject.setProductKey(key);
        productObject.setDetails(details);
        productDomain.patchUpdateProduct(productObject);
    }

    private ProductObject findProduct(String key) {
        ProductObject productObject = productDomain.getProduct(key);
        if (productObject == null) {
            throw new NotFoundException("product not found by key " + key);
        }
        return productObject;
    }

}


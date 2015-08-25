package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.ProductDomain;
import com.yunsoo.api.rabbit.dto.basic.ProductBase;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
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
 * Descriptions: Allow anonymous call!
 * <p>
 * * ErrorCode
 * 40401    :   产品找不到
 * 40402    :   产品Thumbnail找不到
 */
@RestController
@RequestMapping(value = "/productbase")
public class ProductBaseController {

    @Autowired
    private ProductDomain productDomain;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductBase get(@PathVariable(value = "id") String id) {
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("ProductBaseId不应为空！");
        }
        ProductBase productBase = productDomain.getProductBaseById(id);
        if (productBase == null) {
            throw new NotFoundException(40401, "找不到产品");
        }
        return productBase;
    }

    @Deprecated   //todo: to be removed, replace by getProductImage
    @RequestMapping(value = "/{id}/{client}", method = RequestMethod.GET)
    public ResponseEntity<?> getThumbnail(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "client") String client) {
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("Id不应为空！");
        }
        if (client == null || client.isEmpty()) {
            throw new BadRequestException("Client不应为空！");
        }
        // hard code the imageName
        String imageName = "";
        if ("full-mobile".equals(client)) {
            imageName = "image-400x200";
        }
        if ("full-mobile@2x".equals(client)) {
            imageName = "image-800x400";
        }
        if ("logo-mobile".equals(client)) {
            imageName = "image-400x400";
        }
        return getProductBaseImage(id, imageName);

//        try {
//            FileObject fileObject = dataAPIClient.get("productbase/{id}/{client}", FileObject.class, id, client);
//            if (fileObject.getLength() > 0) {
//                return ResponseEntity.ok()
//                        .contentLength(fileObject.getLength())
//                        .contentType(MediaType.parseMediaType(fileObject.getContentType()))
//                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
//            } else {
//                return ResponseEntity.ok()
//                        .contentType(MediaType.parseMediaType(fileObject.getContentType()))
//                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
//            }
//        } catch (NotFoundException ex) {
//            throw new NotFoundException(40402, "找不到产品图片 id = " + id + "  client = " + client);
//        }
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

    @Deprecated   //todo: to be removed, replace by getProductBasedetail
    @RequestMapping(value = "/{id}/{key}/json", method = RequestMethod.GET)
    public ResponseEntity<?> getFileInJson(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "key") String key) {
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("Id不应为空！");
        }
        if (key == null || key.isEmpty()) {
            throw new BadRequestException("Key不应为空！");
        }
        return getProductBaseDetails(id);

//        try {
//            FileObject fileObject = dataAPIClient.get("productbase/{id}/{key}/json", FileObject.class, id, key);
//            if (fileObject.getLength() > 0) {
//                return ResponseEntity.ok()
//                        .contentLength(fileObject.getLength())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
//            } else {
//                return ResponseEntity.ok()
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
//            }
//        } catch (NotFoundException ex) {
//            throw new NotFoundException(40402, "找不到文件 id = " + id + "  key = " + key);
//        }
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

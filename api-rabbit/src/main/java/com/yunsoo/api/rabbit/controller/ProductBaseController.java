package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.ProductDomain;
import com.yunsoo.api.rabbit.dto.basic.ProductBase;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

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
    private RestClient dataAPIClient;

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

    @RequestMapping(value = "/thumbnail/{id}/{client}", method = RequestMethod.GET)
    public ResponseEntity<?> getThumbnail(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "client") String client) {
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("Id不应为空！");
        }
        if (client == null || client.isEmpty()) {
            throw new BadRequestException("Client不应为空！");
        }

        try {
            FileObject fileObject = dataAPIClient.get("productbase/thumbnail/{id}/{client}", FileObject.class, id, client);
            if (fileObject.getLenth() > 0) {
                return ResponseEntity.ok()
                        .contentLength(fileObject.getLenth())
                        .contentType(MediaType.parseMediaType(fileObject.getSuffix()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getThumbnailData())));
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(fileObject.getSuffix()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getThumbnailData())));
            }
        } catch (NotFoundException ex) {
            throw new NotFoundException(40402, "找不到产品图片 id = " + id + "  client = " + client);
        }
    }

}

package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.FileDomain;
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
 * Created on:   2016-04-25
 * Descriptions:
 */
@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private FileDomain fileDomain;


    @RequestMapping(value = "{imageName}", method = RequestMethod.GET)
    public ResponseEntity<?> getImage(@PathVariable("imageName") String imageName) {
        ResourceInputStream resourceInputStream = fileDomain.getFile(String.format("image/%s", imageName));
        if (resourceInputStream == null) {
            throw new NotFoundException("image not found");
        }
        String contentType = resourceInputStream.getContentType();
        long contentLength = resourceInputStream.getContentLength();

        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        if (contentType != null) {
            bodyBuilder.contentType(MediaType.parseMediaType(contentType));
        }
        if (contentLength > 0) {
            bodyBuilder.contentLength(contentLength);
        }
        return bodyBuilder.body(new InputStreamResource(resourceInputStream));
    }
}

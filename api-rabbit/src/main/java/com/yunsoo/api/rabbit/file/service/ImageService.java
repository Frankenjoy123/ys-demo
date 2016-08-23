package com.yunsoo.api.rabbit.file.service;

import com.yunsoo.common.util.ImageProcessor;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by yan on 8/18/2016.
 */
@Service
public class ImageService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private FileService fileService;

    public ResponseEntity getImage(String imagePath){
        ResourceInputStream resourceInputStream = fileService.getFile(imagePath);
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

    public void save(byte[] resultData, String imageName, String contentType) throws IOException {
        fileService.putFile(imageName, new ResourceInputStream(new ByteArrayInputStream(resultData), resultData.length, contentType));
        log.info(String.format("image saved [imageName: %s, contentType: %s, size: %d]", imageName, contentType, resultData.length));
    }



}

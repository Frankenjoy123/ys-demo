package com.yunsoo.api.controller;

import com.yunsoo.api.dto.ImageParameter;
import com.yunsoo.api.dto.ImageResponse;
import com.yunsoo.common.util.ImageProcessor;
import com.yunsoo.common.util.RandomUtils;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by:   Lijian
 * Created on:   2016-01-20
 * Descriptions:
 */
@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private RestClient dataAPIClient;

    private Log log = LogFactory.getLog(this.getClass());


    @RequestMapping(value = "", method = RequestMethod.POST)
    public ImageResponse uploadFile(@RequestParam("file") MultipartFile file,
                                    @ModelAttribute ImageParameter imageParameter) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException();
        }
        long size = file.getSize();
        //String contentType = file.getContentType();
        String imageName = generateImageName();
        byte[] bytes;
        bytes = file.getBytes();

        ImageProcessor imageProcessor = new ImageProcessor().read(new ByteArrayInputStream(bytes));
        if (imageParameter != null) {
            int x = intValue(imageParameter.getX());
            int y = intValue(imageParameter.getY());
            int width = intValue(imageParameter.getWidth());
            int height = intValue(imageParameter.getHeight());
            int srcWidth = imageProcessor.getWidth();
            int srcHeight = imageProcessor.getHeight();
            width = width > 0 ? width : srcWidth - x;
            height = height > 0 ? height : srcHeight - y;
            if (width > 0 && height > 0 && x + width <= srcWidth && y + height <= srcHeight) {
                //crop
                imageProcessor = imageProcessor.crop(x, y, width, height);
            }
        }

        ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();
        imageProcessor.write(imageOutputStream, "jpg");
        dataAPIClient.put("file/s3?path=image/{imageName}", new ResourceInputStream(new ByteArrayInputStream(imageOutputStream.toByteArray()), imageOutputStream.size(), "image/jpg"), imageName);

        log.info(String.format("image uploaded [name: %s]", imageName));

        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setName(imageName);
        imageResponse.setUrl(String.format("/image/%s", imageName));
        return imageResponse;
    }

    @RequestMapping(value = "{imageName}", method = RequestMethod.GET)
    public ResponseEntity<?> getImage(@PathVariable("imageName") String imageName) {
        ResourceInputStream resourceInputStream = dataAPIClient.getResourceInputStream("file/s3?path=image/{imageName}", imageName);
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

    private String generateImageName() {
        return DateTime.now().toDateTime(DateTimeZone.UTC).toString("yyMMddHHmmssSSS") + RandomUtils.generateString(3, RandomUtils.NUMERIC_CHARS);
    }

    private int intValue(Integer value) {
        return value != null && value > 0 ? value : 0;
    }

}

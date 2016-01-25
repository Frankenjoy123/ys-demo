package com.yunsoo.api.controller;

import com.yunsoo.api.domain.FileDomain;
import com.yunsoo.api.dto.ImageRequest;
import com.yunsoo.api.dto.ImageResponse;
import com.yunsoo.common.util.ImageProcessor;
import com.yunsoo.common.util.RandomUtils;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
import org.apache.commons.codec.binary.Base64;
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

import javax.validation.Valid;
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

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private FileDomain fileDomain;


    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ImageResponse uploadImage(@RequestParam("file") MultipartFile file,
                                     @ModelAttribute ImageRequest.Options options) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException();
        }
        //long size = file.getSize();
        //String contentType = file.getContentType();
        byte[] imageDataBytes = file.getBytes();

        ImageProcessor imageProcessor = new ImageProcessor().read(new ByteArrayInputStream(imageDataBytes));

        log.info(String.format("image read [width: %s, height: %s]", imageProcessor.getWidth(), imageProcessor.getHeight()));

        imageProcessor = corpImage(imageProcessor, options);

        String imageName = generateImageName();
        saveImage(imageProcessor, imageName);

        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setName(imageName);
        imageResponse.setUrl(String.format("/image/%s", imageName));
        return imageResponse;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ImageResponse uploadImage(@RequestBody @Valid ImageRequest imageRequest) throws IOException {

        String imageData = imageRequest.getData(); //data:image/png;base64,
        int splitIndex = imageData.indexOf(",");
        //String metaHeader = imageData.substring(0, splitIndex);
        //String contentType = metaHeader.split(";")[0].split(":")[1];
        String imageDataBase64 = imageData.substring(splitIndex + 1);
        byte[] imageDataBytes = Base64.decodeBase64(imageDataBase64);

        ImageProcessor imageProcessor = new ImageProcessor().read(new ByteArrayInputStream(imageDataBytes));

        log.info(String.format("image read [width: %s, height: %s]", imageProcessor.getWidth(), imageProcessor.getHeight()));

        imageProcessor = corpImage(imageProcessor, imageRequest.getOptions());

        String imageName = generateImageName();
        saveImage(imageProcessor, imageName);

        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setName(imageName);
        imageResponse.setUrl(String.format("/image/%s", imageName));
        return imageResponse;
    }

    @RequestMapping(value = "{imageName}", method = RequestMethod.GET)
    public ResponseEntity<?> getImage(@PathVariable("imageName") String imageName) {
        ResourceInputStream resourceInputStream = fileDomain.getFile(String.format("image/%s", imageName));
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

    private ImageProcessor corpImage(ImageProcessor imageProcessor, ImageRequest.Options options) {
        if (options != null) {
            int x = intValue(options.getX());
            int y = intValue(options.getY());
            int width = intValue(options.getWidth());
            int height = intValue(options.getHeight());
            int srcWidth = imageProcessor.getWidth();
            int srcHeight = imageProcessor.getHeight();
            width = width > 0 ? width : srcWidth - x;
            height = height > 0 ? height : srcHeight - y;
            if (width > 0 && height > 0 && x + width <= srcWidth && y + height <= srcHeight) {
                //crop
                imageProcessor = imageProcessor.crop(x, y, width, height);
                log.info(String.format("image cropped [x: %s, y: %s, width: %s, height: %s]", x, y, width, height));
            } else {
                log.warn(String.format("cropping parameters out of range [x: %s, y: %s, width: %s, height: %s]", x, y, width, height));
            }
        } else {
            log.info("ignore cropping because of null options");
        }
        return imageProcessor;
    }

    private void saveImage(ImageProcessor imageProcessor, String imageName) throws IOException {
        ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();
        imageProcessor.write(imageOutputStream, "jpg");
        fileDomain.putFile(String.format("image/%s", imageName), new ResourceInputStream(new ByteArrayInputStream(imageOutputStream.toByteArray()), imageOutputStream.size(), "image/jpg"));
        log.info(String.format("image saved [imageName: %s]", imageName));
    }

}

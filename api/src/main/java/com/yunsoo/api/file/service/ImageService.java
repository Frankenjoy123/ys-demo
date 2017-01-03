package com.yunsoo.api.file.service;

import com.tinify.Tinify;
import com.yunsoo.api.file.dto.ImageRequest;
import com.yunsoo.api.file.dto.ImageResponse;
import com.yunsoo.common.util.ImageProcessor;
import com.yunsoo.common.util.RandomUtils;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by yan on 8/18/2016.
 */
@Service
public class ImageService {

    private Log log = LogFactory.getLog(this.getClass());

    @Value("${yunsoo.tinify.key}")
    private String tinify_key;

    @Autowired
    private FileService fileService;

    public ImageResponse saveImage(byte[] imageDataBytes, String contentType, String imagePath, ImageRequest.Options options, Boolean compress) throws IOException {

        ImageProcessor imageProcessor = new ImageProcessor().read(new ByteArrayInputStream(imageDataBytes));

        log.info(String.format("image read [width: %s, height: %s]", imageProcessor.getWidth(), imageProcessor.getHeight()));

        imageProcessor = corpImage(imageProcessor, options);
        if (!StringUtils.hasText(imagePath))
            imagePath = generateImagePath();

        save(imageProcessor, imagePath, contentType, compress);

        ImageResponse imageResponse = new ImageResponse();
        String[] paths = imagePath.split("/");
        imageResponse.setName(paths[paths.length - 1]);
        imageResponse.setUrl("/" + imagePath);
        imageResponse.setExt(convertImageContentTypeToExt(contentType));
        return imageResponse;
    }

    public ImageResponse saveImage(byte[] imageDataBytes, String contentType, String imagePath) throws IOException {
        if (!StringUtils.hasText(imagePath))
            imagePath = generateImagePath();

        fileService.putFile(imagePath, new ResourceInputStream(new ByteArrayInputStream(imageDataBytes), imageDataBytes.length, contentType));

        ImageResponse imageResponse = new ImageResponse();
        String[] paths = imagePath.split("/");
        imageResponse.setName(paths[paths.length - 1]);
        imageResponse.setUrl("/" + imagePath);
        imageResponse.setExt(convertImageContentTypeToExt(contentType));
        return imageResponse;
    }

    public ResponseEntity getImage(String imagePath) {
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

    private String generateImagePath() {
        String randomName = DateTime.now().toDateTime(DateTimeZone.UTC).toString("yyMMddHHmmssSSS") + RandomUtils.generateString(3, RandomUtils.NUMERIC_CHARS);
        return String.format("image/%s", randomName);
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
            int maxWidth = srcWidth - x;
            int maxHeight = srcHeight - y;
            width = width <= maxWidth ? width : maxWidth;
            height = height <= maxHeight ? height : maxHeight;
            if (x < srcWidth && y < srcHeight && width > 0 && height > 0) {
                //crop
                imageProcessor = imageProcessor.crop(x, y, width, height);
                log.info(String.format("image cropped [x: %s, y: %s, width: %s, height: %s]", x, y, width, height));
            } else {
                log.warn(String.format("cropping parameters out of range [x: %s, y: %s, width: %s, height: %s, srcWidth: %s, srcHeight: %s]", x, y, width, height, srcWidth, srcHeight));
            }
        } else {
            log.info("ignore cropping because of null options");
        }
        return imageProcessor;
    }

    private void save(ImageProcessor imageProcessor, String imageName, String contentType, Boolean compress) throws IOException {
        ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();
        imageProcessor.write(imageOutputStream, contentType);

        byte[] buffer = imageOutputStream.toByteArray();
        byte[] resultData = imageOutputStream.toByteArray();

        if (compress != null && compress) {
            try {
                Tinify.setKey(tinify_key);
                resultData = Tinify.fromBuffer(buffer).toBuffer();
            } catch (Exception e) {
                resultData = imageOutputStream.toByteArray();
            }
        }

        fileService.putFile(imageName, new ResourceInputStream(new ByteArrayInputStream(resultData), resultData.length, contentType));
        log.info(String.format("image saved [imageName: %s, contentType: %s, size: %d]", imageName, contentType, resultData.length));
    }

    private String convertImageContentTypeToExt(String contentType) {
        if (StringUtils.isEmpty(contentType)) {
            return null;
        }
        switch (contentType.toLowerCase()) {
            case "image/jpeg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "image/gif":
                return ".gif";
            case "image/tiff":
                return ".tiff";
            case "image/bmp":
                return ".bmp";
            default:
                log.error(String.format("contentType '%s' of image not recognized", contentType));
                return null;
        }
    }

}

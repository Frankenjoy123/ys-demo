package com.yunsoo.api.controller;

import com.yunsoo.api.file.dto.ImageRequest;
import com.yunsoo.api.file.dto.ImageResponse;
import com.yunsoo.api.file.service.ImageService;
import com.yunsoo.common.web.exception.BadRequestException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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
    private ImageService imageService;

    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ImageResponse uploadImage(@RequestParam("file") MultipartFile file,
                                     @RequestParam(value = "compress", required = false) Boolean compress,
                                     @ModelAttribute ImageRequest.Options options) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException();
        }
        String contentType = file.getContentType();
        byte[] imageDataBytes = file.getBytes();

        return imageService.saveImage(imageDataBytes, contentType, null, options, compress);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ImageResponse uploadImage(
            @RequestParam(value = "compress", required = false) Boolean compress,
            @RequestBody @Valid ImageRequest imageRequest) throws IOException {

        String imageData = imageRequest.getData(); //data:image/png;base64,
        int splitIndex = imageData.indexOf(",");
        String metaHeader = imageData.substring(0, splitIndex);
        String contentType = metaHeader.split(";")[0].split(":")[1];
        String imageDataBase64 = imageData.substring(splitIndex + 1);
        byte[] imageDataBytes = Base64.decodeBase64(imageDataBase64);

        return imageService.saveImage(imageDataBytes, contentType, null, imageRequest.getOptions(), compress);
    }

    @RequestMapping(value = "{imageName}", method = RequestMethod.GET)
    public ResponseEntity<?> getImage(@PathVariable("imageName") String imageName) {
       return imageService.getImage(String.format("image/%s", imageName));
    }



}

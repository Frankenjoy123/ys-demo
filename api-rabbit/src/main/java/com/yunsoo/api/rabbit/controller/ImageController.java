package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.file.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ImageService imageService;

    @RequestMapping(value = "{imageName}", method = RequestMethod.GET)
    public ResponseEntity<?> getImage(@PathVariable("imageName") String imageName) {
        return imageService.getImage(String.format("image/%s", imageName));
    }
}

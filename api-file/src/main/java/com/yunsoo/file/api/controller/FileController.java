package com.yunsoo.file.api.controller;

import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-16
 * Descriptions:
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getFileByPath(@RequestParam(value = "path", required = true) String path) throws IOException {
        if (StringUtils.isEmpty(path)) {
            throw new BadRequestException("path must not be null or empty");
        }
        ResourceInputStream file = fileService.getFileByPath(path);
        if (file == null) {
            throw new NotFoundException("file not found by path: " + path);
        }

        String contentType = file.getContentType();
        long contentLength = file.getContentLength();

        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        if (contentType != null) {
            bodyBuilder.contentType(MediaType.parseMediaType(contentType));
        }
        if (contentLength > 0) {
            bodyBuilder.contentLength(contentLength);
        }
        return bodyBuilder.body(new InputStreamResource(file));
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void putFileByPath(@RequestParam(value = "path", required = true) String path,
                              HttpServletRequest request) throws IOException {
        if (StringUtils.isEmpty(path)) {
            throw new BadRequestException("path must not be null or empty");
        }
        InputStream inputStream = request.getInputStream();
        long contentLength = request.getContentLength();
        String contentType = request.getContentType();
        ResourceInputStream file = new ResourceInputStream(inputStream, contentLength, contentType);

        fileService.putFileToPath(path, file);
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public List<String> getFileNamesByFolderPath(@RequestParam(value = "path", required = true) String path) throws IOException {
        if (StringUtils.isEmpty(path)) {
            throw new BadRequestException("path must not be null or empty");
        }
        return fileService.getFileNamesByFolderName(path);
    }
}

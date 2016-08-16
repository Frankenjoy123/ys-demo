package com.yunsoo.key.api.controller;

import com.yunsoo.common.web.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by:   Lijian
 * Created on:   2016-08-16
 * Descriptions:
 */
@RestController
@RequestMapping("/file")
public class FileController {


    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getFileByPath(@RequestParam(value = "path", required = true) String path) throws IOException {
        if (StringUtils.isEmpty(path)) {
            throw new BadRequestException("path must not be null or empty");
        }
        //todo
        return null;

    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void putFileByPath(@RequestParam(value = "path", required = true) String path,
                              HttpServletRequest request) throws IOException {
        if (StringUtils.isEmpty(path)) {
            throw new BadRequestException("path must not be null or empty");
        }
        //todo

    }
}

package com.yunsoo.api.controller;

import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

/**
 * Created by Zhe on 2015/6/12.
 * * ErrorCode
 * 40401    :   File Not found!
 */

@RestController
@RequestMapping(value = "/report")
public class ReportController {

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;
    @Autowired
    private RestClient dataAPIClient;
    private String folder = "report";

    @RequestMapping(value = "myorganization/{type}/{period}", method = RequestMethod.GET)
    public ResponseEntity get(@PathVariable(value = "type") String type,
                              @PathVariable(value = "period") String period) {
        String currentOrgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        String theFilePath = folder + "/organization/" + currentOrgId + "/" + type + "/" + period;
        try {
            FileObject fileObject = dataAPIClient.get("file?path={0}", FileObject.class, theFilePath);
            if (fileObject.getLength() > 0) {
                return ResponseEntity.ok()
                        .contentLength(fileObject.getLength())
                        .contentType(MediaType.parseMediaType(fileObject.getContentType()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(fileObject.getContentType()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
            }
        } catch (NotFoundException ex) {
            throw new NotFoundException(40401, "找不到文件 = " + theFilePath);
        }
    }
}


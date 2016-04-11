package com.yunsoo.api.controller;

import com.yunsoo.api.domain.FileDomain;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Zhe
 * Created on:   2015/6/12
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/report")
public class ReportController {

    @Autowired
    private FileDomain fileDomain;

    @RequestMapping(value = "myorganization/{type}/{period}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission('current', 'org', 'reports:read')")
    public ResponseEntity get(@PathVariable(value = "type") String type,
                              @PathVariable(value = "period") String period) {
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        String path = String.format("report/organization/%s/%s/%s", orgId, type, period);
        ResourceInputStream resourceInputStream = fileDomain.getFile(path);
        if (resourceInputStream == null) {
            throw new NotFoundException("report not found");
        }
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        bodyBuilder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
        bodyBuilder.contentLength(resourceInputStream.getContentLength());
        return bodyBuilder.body(new InputStreamResource(resourceInputStream));
    }
}


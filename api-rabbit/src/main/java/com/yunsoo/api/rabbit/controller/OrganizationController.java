package com.yunsoo.api.rabbit.controller;

import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

/**
 * Created by Zhe on 2015/4/2.
 * Allow anonymous call.
 */
@RestController
@RequestMapping(value = "/organization")
public class OrganizationController {

    @Autowired
    private RestClient dataAPIClient;

    @RequestMapping(value = "/{id}/thumbnail/{imageKey}", method = RequestMethod.GET)
    public ResponseEntity<?> getThumbnail(
            @PathVariable(value = "id") Long id,
            @PathVariable(value = "imageKey") String imageKey) {
        if (id == null || id <= 0) throw new BadRequestException("ID不能小于0！");
        if (imageKey == null || imageKey.isEmpty()) throw new BadRequestException("imageKey 不能为空！");
        try {
            FileObject fileObject = dataAPIClient.get("organization/{id}/thumbnail/{imageKey}", FileObject.class, id, imageKey);
            if (fileObject.getLength() > 0) {
                return ResponseEntity.ok()
                        .contentLength(fileObject.getLength())
                        .contentType(MediaType.parseMediaType(fileObject.getSuffix()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getThumbnailData())));
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(fileObject.getSuffix()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getThumbnailData())));
            }
        } catch (NotFoundException ex) {
            throw new NotFoundException(40402, "找不到组织图片 id = " + id + "  client = " + imageKey);
        }
    }
}

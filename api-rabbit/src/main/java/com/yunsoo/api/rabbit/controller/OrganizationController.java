package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.OrganizationDomain;
import com.yunsoo.api.rabbit.dto.basic.Organization;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Zhe on 2015/4/2.
 * Allow anonymous call.
 */
@RestController
@RequestMapping(value = "/organization")
public class OrganizationController {

    @Autowired
    private OrganizationDomain organizationDomain;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Organization getOrganizationById(@PathVariable(value = "id") String id) {
        OrganizationObject object = organizationDomain.getById(id);
        if (object == null) {
            throw new NotFoundException("organization not found by [id: " + id + "]");
        }
        return new Organization(object);
    }

    @Deprecated //todo: to be removed, replace by getLogo
    @RequestMapping(value = "/{id}/{imageKey}", method = RequestMethod.GET)
    public ResponseEntity<?> getThumbnail(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "imageKey") String imageKey) {
        String imageName = "image-128x128"; //hard coded, this method should not be used anymore
        return getLogo(id, imageName);
    }

    @RequestMapping(value = "{id}/logo/{imageName}", method = RequestMethod.GET)
    public ResponseEntity<?> getLogo(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "imageName") String imageName) {
        ResourceInputStream resourceInputStream = organizationDomain.getLogoImage(id, imageName);
        if (resourceInputStream == null) {
            throw new NotFoundException("logo not found");
        }
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
        if (resourceInputStream.getContentLength() > 0) {
            builder.contentLength(resourceInputStream.getContentLength());
        }
        return builder.body(new InputStreamResource(resourceInputStream));
    }

}

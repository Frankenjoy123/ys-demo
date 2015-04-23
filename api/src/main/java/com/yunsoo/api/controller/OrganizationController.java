package com.yunsoo.api.controller;

import com.yunsoo.api.dto.basic.Organization;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

/**
 * Created by  : Zhe
 * Created on  : 2015/4/2
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/organization")
public class OrganizationController {

    @Autowired
    private RestClient dataAPIClient;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Organization getOrganizationById(@PathVariable(value = "id") String id) {
        OrganizationObject object = dataAPIClient.get("organization/{id}", OrganizationObject.class, id);
        if (object == null) {
            throw new NotFoundException("organization not found by [id: " + id + "]");
        }
        return fromOrganizationObject(object);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Organization create(@RequestBody Organization organization) {
        OrganizationObject object = toOrganizationObject(organization);
        object.setId(null);
        OrganizationObject newObject = dataAPIClient.post("organization", object, OrganizationObject.class);
        return fromOrganizationObject(newObject);
    }

    @RequestMapping(value = "/{id}/thumbnail/{imageKey}", method = RequestMethod.GET)
    public ResponseEntity<?> getThumbnail(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "imageKey") String imageKey) {
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

    private OrganizationObject toOrganizationObject(Organization organization) {
        OrganizationObject object = new OrganizationObject();
        object.setId(organization.getId());
        object.setName(organization.getName());
        object.setStatusCode(organization.getStatusCode());
        object.setDescription(organization.getDescription());
        object.setTypeCode(organization.getTypeCode());
        object.setImageUri(organization.getImageUri());
        object.setDetails(organization.getDetails());
        object.setCreatedAccountId(organization.getCreatedAccountId());
        object.setCreatedDateTime(organization.getCreatedDateTime());
        return object;
    }

    private Organization fromOrganizationObject(OrganizationObject object) {
        Organization entity = new Organization();
        entity.setId(object.getId());
        entity.setName(object.getName());
        entity.setStatusCode(object.getStatusCode());
        entity.setDescription(object.getDescription());
        entity.setTypeCode(object.getTypeCode());
        entity.setImageUri(object.getImageUri());
        entity.setDetails(object.getDetails());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        return entity;
    }
}

package com.yunsoo.api.controller;

import com.yunsoo.api.domain.OrganizationDomain;
import com.yunsoo.api.dto.basic.Organization;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private OrganizationDomain organizationDomain;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'organization:read')")
    public Organization getOrganizationById(@PathVariable(value = "id") String id) {
        OrganizationObject object = organizationDomain.getOrganizationById(id);
        if (object == null) {
            throw new NotFoundException("organization not found by [id: " + id + "]");
        }
        return fromOrganizationObject(object);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'organization:read')")
    public Organization getByFilter(@RequestParam(value = "name") String name) {
        OrganizationObject object = organizationDomain.getOrganizationByName(name);
        if (object == null) {
            throw new NotFoundException("organization not found by [name: " + name + "]");
        }
        return fromOrganizationObject(object);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#organization.id, 'filterByOrg', 'organization:create')")
    public Organization create(@RequestBody Organization organization) {
        OrganizationObject object = toOrganizationObject(organization);
        object.setId(null);
        OrganizationObject newObject = dataAPIClient.post("organization", object, OrganizationObject.class);
        return fromOrganizationObject(newObject);
    }

    @RequestMapping(value = "/{id}/{imageKey}", method = RequestMethod.GET)
//    @PreAuthorize("hasPermission(#id, 'filterByOrg', 'organization:read')")
    public ResponseEntity<?> getThumbnail(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "imageKey") String imageKey) {
        try {
            FileObject fileObject = dataAPIClient.get("organization/{id}/{imageKey}", FileObject.class, id, imageKey);
            if (fileObject.getLength() > 0) {
                return ResponseEntity.ok()
                        .contentLength(fileObject.getLength())
                        .contentType(MediaType.parseMediaType(fileObject.getSuffix()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(fileObject.getSuffix()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
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
        entity.setDetails(object.getDetails());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        return entity;
    }
}

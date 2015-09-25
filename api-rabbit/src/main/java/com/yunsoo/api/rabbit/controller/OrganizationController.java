package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.OrganizationDomain;
import com.yunsoo.api.rabbit.domain.ProductBaseDomain;
import com.yunsoo.api.rabbit.domain.ProductDomain;
import com.yunsoo.api.rabbit.dto.Organization;
import com.yunsoo.api.rabbit.dto.ProductBase;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Zhe on 2015/4/2.
 * Allow anonymous call.
 */
@RestController
@RequestMapping(value = "/organization")
public class OrganizationController {

    @Autowired
    private OrganizationDomain organizationDomain;

    @Autowired
    private ProductBaseDomain productBaseDomain;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Organization getOrganizationById(@PathVariable(value = "id") String id) {
        OrganizationObject object = organizationDomain.getById(id);
        if (object == null) {
            throw new NotFoundException("organization not found by [id: " + id + "]");
        }
        return new Organization(object);
    }
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Collection<Organization> getByFilter(){
        List<ProductBase> productBaseList = productBaseDomain.getAllProductBases();
        HashMap<String, Organization> orgList = new HashMap<>();
        productBaseList.forEach(item->{
            String orgId = item.getOrganization().getId();
            if(!orgList.containsKey(orgId)) {
                Organization org = item.getOrganization();
                org.setProductBaseList(new ArrayList<ProductBase>());
                orgList.put(orgId, org);
            }
            item.setOrganization(null);
            orgList.get(orgId).getProductBaseList().add(item);
        });

        return orgList.values();
    }


    @RequestMapping(value = "{id}/logo", method = RequestMethod.GET)
    public ResponseEntity<?> getOrganizationLogo(
            @PathVariable(value = "id") String id,
            @RequestParam(value = "image_name", required = false) String imageName) {
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

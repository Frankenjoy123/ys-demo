package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.auth.service.AuthOrganizationService;
import com.yunsoo.api.rabbit.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.rabbit.dto.Organization;
import com.yunsoo.api.rabbit.dto.ProductBase;
import com.yunsoo.api.rabbit.file.service.FileService;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/12
 * Descriptions:
 */
@Component
@ObjectCacheConfig
public class OrganizationDomain {

    private static final String DEFAULT_LOGO_IMAGE_NAME = "image-128x128";

    @Autowired
    private RestClient dataApiClient;

    @Autowired
    private FileService fileService;

    @Autowired
    private AuthOrganizationService authOrganizationService;

    public List<Organization> getOrganizationList() {
        List<com.yunsoo.api.rabbit.auth.dto.Organization> orgObjList = authOrganizationService.getAllBrand();
        List<ProductBaseObject> productBaseList = dataApiClient.get("productbase", new ParameterizedTypeReference<List<ProductBaseObject>>() {
        });
        if (orgObjList != null && productBaseList != null) {
            List<Organization> orgList = new ArrayList<>();
            orgObjList.forEach(item -> {
                Organization organization = new Organization(item);
                organization.setProductBaseList(productBaseList.stream().filter(product -> product.getOrgId().equals(item.getId())).map(ProductBase::new).collect(Collectors.toList()));
                orgList.add(organization);
            });

            orgList.removeIf(item -> item.getProductBaseList().size() == 0);

            return orgList;
        }
        return null;
    }

    public ResourceInputStream getLogoImage(String orgId, String imageName) {
        if (StringUtils.isEmpty(imageName)) {
            imageName = DEFAULT_LOGO_IMAGE_NAME;
        }
        if (StringUtils.isEmpty(orgId)) {
            return null;
        }
        String path = String.format("organization/%s/logo/%s", orgId, imageName);
        return fileService.getFile(path);
    }

}

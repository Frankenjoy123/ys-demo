package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.cache.annotation.ElastiCacheConfig;
import com.yunsoo.api.rabbit.dto.Organization;
import com.yunsoo.api.rabbit.dto.ProductBase;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
@ElastiCacheConfig
public class OrganizationDomain {

    private Log log = LogFactory.getLog(this.getClass());

    private static final String DEFAULT_LOGO_IMAGE_NAME = "image-128x128";

    @Autowired
    private RestClient dataAPIClient;

    @Cacheable(key="T(com.yunsoo.api.rabbit.cache.CustomKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).ORGANIZATION.toString(),#id )")
    public OrganizationObject getById(String id) {
        log.debug("cache missing on organization." + id);
        try {
            return dataAPIClient.get("organization/{id}", OrganizationObject.class, id);
        } catch (NotFoundException ex) {
            return null;
        }
    }


    public List<Organization> getOrganizationList() {
        List<OrganizationObject> orgObjList = dataAPIClient.get("organization", new ParameterizedTypeReference<List<OrganizationObject>>() { });
        List<ProductBaseObject> productBaseList = dataAPIClient.get("productbase", new ParameterizedTypeReference<List<ProductBaseObject>>() {});
        if(orgObjList != null && productBaseList != null){
            List<Organization> orgList = new ArrayList<>();
            orgObjList.forEach(item -> {
                Organization organization = new Organization(item);
                organization.setProductBaseList(productBaseList.stream().filter(product->product.getOrgId().equals(item.getId())).map(ProductBase::new).collect(Collectors.toList()));
                orgList.add(organization);
            });

            orgList.removeIf( item -> item.getProductBaseList().size()==0);

           return orgList;
        }
       return null;
    }

    public ResourceInputStream getLogoImage(String orgId, String imageName) {
        if (StringUtils.isEmpty(imageName)) {
            imageName = DEFAULT_LOGO_IMAGE_NAME;
        }
        try {
            return dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/logo/{imageName}", orgId, imageName);
        } catch (NotFoundException ex) {
            return null;
        }
    }
}

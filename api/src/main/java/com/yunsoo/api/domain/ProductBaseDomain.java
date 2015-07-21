package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/1
 * Descriptions:
 */
@Component
public class ProductBaseDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private LookupDomain lookupDomain;

    @Autowired
    private ProductCategoryDomain productCategoryDomain;


    public ProductBaseObject getProductBaseById(String productBaseId) {
        try {
            return dataAPIClient.get("productbase/{id}", ProductBaseObject.class, productBaseId);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public List<ProductBaseObject> getProductBaseByOrgId(String orgId) {
        return dataAPIClient.get("productbase?org_id={orgId}", new ParameterizedTypeReference<List<ProductBaseObject>>() {
        }, orgId);
    }

}

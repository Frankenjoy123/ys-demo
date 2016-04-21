package com.yunsoo.api.domain;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by:   Lijian
 * Created on:   2015/3/20
 * Descriptions:
 */
@Component
public class ProductDomain {

    @Autowired
    private RestClient dataAPIClient;


    public ProductObject getProduct(String key) {
        try {
            return dataAPIClient.get("product/{key}", ProductObject.class, key);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public void activeProduct(String key) {
        ProductObject productObject = new ProductObject();
        productObject.setProductStatusCode(LookupCodes.ProductStatus.ACTIVATED);
        dataAPIClient.patch("product/{key}", productObject, key);
    }

    public void deleteProduct(String key) {
        ProductObject productObject = new ProductObject();
        productObject.setProductStatusCode(LookupCodes.ProductStatus.DELETED);
        dataAPIClient.patch("product/{key}", productObject, key);
    }

    public void patchUpdateProduct(String key, ProductObject productObject) {
        dataAPIClient.patch("product/{key}", productObject, key);
    }

}

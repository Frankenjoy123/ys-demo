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
    private RestClient dataApiClient;


    public ProductObject getProduct(String key) {
        try {
            return dataApiClient.get("product/{key}", ProductObject.class, key);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public void activeProduct(String key) {
        ProductObject productObject = new ProductObject();
        productObject.setProductKey(key);
        productObject.setProductStatusCode(LookupCodes.ProductStatus.ACTIVATED);
        patchUpdateProduct(productObject);
    }

    public void deleteProduct(String key) {
        ProductObject productObject = new ProductObject();
        productObject.setProductKey(key);
        productObject.setProductStatusCode(LookupCodes.ProductStatus.DELETED);
        patchUpdateProduct(productObject);
    }

    public void patchUpdateProduct(ProductObject productObject) {
        if (productObject != null && productObject.getProductKey() != null) {
            try {
                dataApiClient.patch("product/{key}", productObject, productObject.getProductKey());
            } catch (NotFoundException ex) {
                throw new NotFoundException("product not found by key " + productObject.getProductKey());
            }
        }
    }

}

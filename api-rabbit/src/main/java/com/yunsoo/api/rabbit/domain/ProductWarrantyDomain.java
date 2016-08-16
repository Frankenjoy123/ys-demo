package com.yunsoo.api.rabbit.domain;

import com.yunsoo.common.data.object.ProductWarrantyObject;
import com.yunsoo.common.web.client.RestClient;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductWarrantyDomain {

    @Autowired
    private RestClient dataApiClient;

    public ProductWarrantyObject createProductWarranty(ProductWarrantyObject productWarrantyObject) {
        productWarrantyObject.setId(null);
        productWarrantyObject.setStatusCode("new");
        productWarrantyObject.setCreatedDateTime(DateTime.now());
        return dataApiClient.post("productwarranty", productWarrantyObject, ProductWarrantyObject.class);
    }
}

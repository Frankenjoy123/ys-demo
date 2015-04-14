package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.ProductFileObject;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Jerry on 4/14/2015.
 */
public class ProductFileDomain {

    @Autowired
    private RestClient dataAPIClient;

    public void createProductFile(ProductFileObject productFileObject) {
        dataAPIClient.post("productfile/", productFileObject, Long.class);
    }
}

package com.yunsoo.processor.domain;

import com.yunsoo.common.data.object.ProductPackageObject;
import com.yunsoo.common.web.client.RestClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-06-24
 * Descriptions:
 */
@Component
public class ProductPackageDomain {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private RestClient dataAPIClient;


    public int batchPackage(List<ProductPackageObject> productPackageObjects) {
        return dataAPIClient.post("productpackage/batch", productPackageObjects, int.class);
    }

}
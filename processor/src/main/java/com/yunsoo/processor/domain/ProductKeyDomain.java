package com.yunsoo.processor.domain;

import com.yunsoo.common.data.object.ProductKeyBatchCreateObject;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.support.YSFile;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-04-26
 * Descriptions:
 */
@Component
public class ProductKeyDomain {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private FileDomain fileDomain;


    public ProductKeyBatchObject getProductKeyBatchById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        try {
            return dataAPIClient.get("productkeybatch/{id}", ProductKeyBatchObject.class, id);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public List<List<String>> getProductKeys(ProductKeyBatchObject productKeyBatchObject) {
        String path = String.format("organization/%s/product_key_batch/%s/keys.pks", productKeyBatchObject.getOrgId(), productKeyBatchObject.getId());
        YSFile ysFile = fileDomain.getYSFile(path);
        if (ysFile == null) {
            return null;
        }
        byte[] content = ysFile.getContent();
        List<List<String>> result = new ArrayList<>();
        String contentStr = new String(content, StandardCharsets.UTF_8);
        String[] lines = contentStr.split("\r\n");
        for (String line : lines) {
            if (line.length() > 0) {
                result.add(Arrays.asList(StringUtils.commaDelimitedListToStringArray(line)));
            }
        }
        String quantity = ysFile.getHeader("quantity");
        if (quantity != null && Integer.parseInt(quantity) == result.size()) {
            return result;
        } else {
            log.error(String.format("getProductKeys result size not equal to quantity [path: %s]", path));
            return null;
        }
    }

    public void updateProductKeyBatchStatus(String productKeyBatchId, String productKeyBatchStatusCode) {
        ProductKeyBatchObject productKeyBatchObject = new ProductKeyBatchObject();
        productKeyBatchObject.setStatusCode(productKeyBatchStatusCode);
        dataAPIClient.patch("productkeybatch/{id}", productKeyBatchObject, productKeyBatchId);
    }

    public void batchCreateProductKeys(ProductKeyBatchObject productKeyBatchObject, List<List<String>> productKeys, String productStatusCode) {
        ProductKeyBatchCreateObject requestObject = new ProductKeyBatchCreateObject();
        requestObject.setProductKeyBatchId(productKeyBatchObject.getId());
        requestObject.setProductKeyTypeCodes(productKeyBatchObject.getProductKeyTypeCodes());
        requestObject.setProductKeys(productKeys);
        requestObject.setProductBaseId(productKeyBatchObject.getProductBaseId());
        requestObject.setProductStatusCode(productStatusCode);
        requestObject.setCreatedDateTime(productKeyBatchObject.getCreatedDateTime());

        dataAPIClient.put("productkey/batch", requestObject);
    }
}

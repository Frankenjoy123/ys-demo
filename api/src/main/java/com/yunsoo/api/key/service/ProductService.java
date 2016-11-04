package com.yunsoo.api.key.service;

import com.yunsoo.api.client.KeyApiClient;
import com.yunsoo.api.key.Constants;
import com.yunsoo.api.key.dto.Key;
import com.yunsoo.api.key.dto.Product;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-11-03
 * Descriptions:
 */
@Service
public class ProductService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private KeyApiClient keyApiClient;

    @Autowired
    private KeyService keyService;

    @Autowired
    private KeyBatchService keyBatchService;


    public Product getProductByKey(String key) {
        if (!StringUtils.hasText(key)) {
            return null;
        }
        try {
            return keyApiClient.get("product/{key}", Product.class, key);
        } catch (NotFoundException e) {
            log.warn("key not found, key: " + key);
            return null;
        }
    }

    public void patchUpdate(Product product) {
        if (product == null || StringUtils.isEmpty(product.getKey())) {
            return;
        }
        try {
            keyApiClient.patch("product/{key}", product, product.getKey());
        } catch (NotFoundException e) {
            log.warn("key not found while patch updating, key: " + product.getKey());
        }
    }

    public void setProductStatusByKey(String key, String statusCode) {
        if (!Constants.ProductStatus.ALL.contains(statusCode)) {
            throw new BadRequestException("status_code invalid");
        }
        Product product = new Product();
        product.setKey(key);
        product.setStatusCode(statusCode);
        patchUpdate(product);
    }

    public void setProductStatusByExternalKey(String partitionId, String externalKey, String statusCode) {
        Assert.hasText(partitionId, "partitionId must not be null or empty");
        Assert.hasText(externalKey, "externalKey must not be null or empty");
        if (!Constants.ProductStatus.ALL.contains(statusCode)) {
            throw new BadRequestException("status_code invalid");
        }

        boolean success = false;
        //all partitionIds
        if ("*".equals(partitionId)) {
            List<String> partitionIds = keyBatchService.getPartitionIdsByOrgId(AuthUtils.getCurrentAccount().getOrgId());
            for (String pId : partitionIds) {
                Key key = keyService.getExternalKey(pId, externalKey);
                if (key != null && !key.isDisabled()) {
                    setProductStatusByKey(key.getPrimaryKey(), statusCode);
                    success = true;
                }
            }
        } else {
            Key key = keyService.getExternalKey(partitionId, externalKey);
            if (key != null && !key.isDisabled()) {
                setProductStatusByKey(key.getPrimaryKey(), statusCode);
                success = true;
            }
        }
        if (!success) {
            throw new NotFoundException(String.format("product not found by key: %s/%s", partitionId, externalKey));
        }
    }

}

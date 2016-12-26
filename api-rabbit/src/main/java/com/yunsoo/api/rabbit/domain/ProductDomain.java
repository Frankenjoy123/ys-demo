package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.rabbit.file.service.FileService;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.util.RandomUtils;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Created by:   Lijian
 * Created on:   2015/3/20
 * Descriptions:
 */
@Component
@ObjectCacheConfig
public class ProductDomain {

    @Autowired
    private RestClient dataApiClient;

    @Autowired
    ProductBaseDomain productBaseDomain;

    @Autowired
    private FileService fileService;


    //@Cacheable(key = "T(com.yunsoo.api.rabbit.cache.CustomKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PRODUCT_BATCH.toString(),#productKeyBatchId)")
    public ProductKeyBatchObject getProductKeyBatch(String productKeyBatchId) {
        try {
            return dataApiClient.get("productkeybatch/{id}", ProductKeyBatchObject.class, productKeyBatchId);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public String getProductKeyBatchDetails(String orgId, String productKeyBatchId) {
        try {
            String path = String.format("organization/%s/product_key_batch/%s/details.json",
                    orgId, productKeyBatchId);
            ResourceInputStream resourceInputStream = fileService.getFile(path);
            if(resourceInputStream == null)
                return null;

            byte[] bytes = StreamUtils.copyToByteArray(resourceInputStream);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (NotFoundException | IOException ignored) {
            return null;
        }
    }

    public Long getCommentsScore(String productBaseId) {
        return dataApiClient.get("productcomments/avgscore/{id}", Long.class, productBaseId);
    }

    @Cacheable(key = "T(com.yunsoo.api.rabbit.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PRODUCTKEYSCENARIO.toString(), #ScenarioId)")
    public String getKeyFromRadis(Number ScenarioId, String key){
        return key;
    }

    @CacheEvict(key = "T(com.yunsoo.api.rabbit.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PRODUCTKEYSCENARIO.toString(), #ScenarioId)")
    public void clearKeyToRadis(Number ScenarioId){

    }


}

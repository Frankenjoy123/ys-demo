package com.yunsoo.service;

import com.yunsoo.ServiceConfig;
import com.yunsoo.service.contract.ProductKeyBatchCreateRequest;
import com.yunsoo.service.contract.ProductKeyBatchCreateResponse;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by:   Lijian
 * Created on:   2015/2/4
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
public class ProductKeyServiceTest {

    @Autowired
    private ProductKeyService productKeyService;

    @Test
    public void test_batchCreate() throws Exception {
        ProductKeyBatchCreateRequest request = new ProductKeyBatchCreateRequest();
        request.setQuantity(5);
        request.setStatusId(0);
        request.setProductKeyTypeIds(new int[]{1, 2});
        request.setCreatedClientId(100);
        request.setCreatedAccountId(1000);
        request.setCreatedDateTime(DateTime.now());
        ProductKeyBatchCreateResponse response = productKeyService.batchCreate(request);
        System.out.println(response.getBatchId());
    }
}

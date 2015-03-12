package com.yunsoo.service;

import com.yunsoo.service.contract.ProductKeyBatch;
import com.yunsoo.util.SpringAppContextUtil;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

/**
 * Created by:   Lijian
 * Created on:   2015/2/6
 * Descriptions:
 */
public class ProductKeyBatchServiceTest {

    private ProductKeyBatchService productKeyBatchService;

    @Before
    public void setUp() throws Exception {
        ApplicationContext applicationContext = SpringAppContextUtil.getApplicationContext();
        productKeyBatchService = (ProductKeyBatchService) applicationContext.getBean("productKeyBatchService");
    }

    @Test
    public void test_getById() throws Exception {
        ProductKeyBatch batch = productKeyBatchService.getById(1);
        System.out.println((batch != null));
        assert true;
    }

    @Test
    public void test_create() throws Exception {
        ProductKeyBatch keyBatch = new ProductKeyBatch();
        keyBatch.setQuantity(5);
        keyBatch.setStatusId(0);
        keyBatch.setCreatedClientId(100);
        keyBatch.setCreatedAccountId(1000);
        keyBatch.setCreatedDateTime(DateTime.now());
        keyBatch.setProductKeyTypeIds(Arrays.asList(1, 2));
        ProductKeyBatch response = productKeyBatchService.create(keyBatch);
        int batchId = response.getId();
        System.out.println(batchId);
        ProductKeyBatch batch = productKeyBatchService.getById(batchId);
        assert batch != null;
    }

}

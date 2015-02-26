package com.yunsoo.service;

import com.yunsoo.service.contract.ProductKeyBatch;
import com.yunsoo.util.SpringDaoUtil;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 * Created by:   Lijian
 * Created on:   2015/2/6
 * Descriptions:
 */
public class ProductKeyBatchServiceTest {

    private ProductKeyBatchService productKeyBatchService;

    @Before
    public void setUp() throws Exception {
        ApplicationContext applicationContext = SpringDaoUtil.getApplicationContext();
        productKeyBatchService = (ProductKeyBatchService) applicationContext.getBean("productKeyBatchService");
    }

    @Test
    public void test_getById() throws Exception {
        ProductKeyBatch batch = productKeyBatchService.getById("01365559-8d51-4dfa-b43e-8fa540a06e18");
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
        keyBatch.setProductKeyTypeIds(new int[]{1, 2});
        ProductKeyBatch response = productKeyBatchService.create(keyBatch);
        String batchId = response.getId();
        System.out.println(batchId);
        ProductKeyBatch batch = productKeyBatchService.getById(batchId);
        assert batch != null;
    }

}

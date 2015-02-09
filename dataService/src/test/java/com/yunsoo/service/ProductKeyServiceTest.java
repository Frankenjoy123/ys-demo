package com.yunsoo.service;

import com.yunsoo.dao.util.SpringDaoUtil;
import com.yunsoo.service.contract.ProductKeyBatch;
import com.yunsoo.service.contract.ProductKeyBatchCreateRequest;
import com.yunsoo.service.contract.ProductKeyBatchCreateResponse;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by:   Lijian
 * Created on:   2015/2/4
 * Descriptions:
 */
//@RunWith(SpringJUnit4ClassRunner.class)
public class ProductKeyServiceTest {

    private ProductKeyService productKeyService;

    private ProductKeyBatchService productKeyBatchService;

    @Before
    public void setUp() throws Exception {
        ApplicationContext applicationContext = SpringDaoUtil.getApplicationContext();
        productKeyService = (ProductKeyService) applicationContext.getBean("productKeyService");
        productKeyBatchService = (ProductKeyBatchService) applicationContext.getBean("productKeyBatchService");
    }

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
        String batchId = response.getBatchId();
        ProductKeyBatch batch = productKeyBatchService.getById(batchId);
        assert batch != null;
    }
}

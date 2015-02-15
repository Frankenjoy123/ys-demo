package com.yunsoo.service;

import com.yunsoo.dao.util.SpringDaoUtil;
import com.yunsoo.service.contract.ProductKeyBatch;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 * Created by:   Lijian
 * Created on:   2015/2/6
 * Descriptions:
 */
//@RunWith(SpringJUnit4ClassRunner.class)
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
}

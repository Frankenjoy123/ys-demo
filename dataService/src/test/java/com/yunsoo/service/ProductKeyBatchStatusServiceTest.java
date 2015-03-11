package com.yunsoo.service;

import com.yunsoo.service.contract.ProductKeyBatchStatus;
import com.yunsoo.util.SpringAppContextUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/3/11
 * Descriptions:
 */
public class ProductKeyBatchStatusServiceTest {

    private ProductKeyBatchStatusService productKeyBatchStatusService;

    @Before
    public void setUp() throws Exception {
        ApplicationContext applicationContext = SpringAppContextUtil.getApplicationContext();
        productKeyBatchStatusService = (ProductKeyBatchStatusService) applicationContext.getBean("productKeyBatchStatusService");
    }

    @Test
    public void test_getAll() {
        List<ProductKeyBatchStatus> statusList = productKeyBatchStatusService.getAll();
        System.out.println("####");
        System.out.println(statusList.stream().map(ProductKeyBatchStatus::getCode).collect(Collectors.toList()));
    }
}

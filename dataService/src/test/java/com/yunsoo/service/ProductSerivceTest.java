package com.yunsoo.service;

import com.yunsoo.service.contract.Product;
import com.yunsoo.util.SpringAppContextUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/12
 * Descriptions:
 */
public class ProductSerivceTest {

    private ProductService productService;

    @Before
    public void setUp() throws Exception {
        ApplicationContext applicationContext = SpringAppContextUtil.getApplicationContext();
        productService = (ProductService) applicationContext.getBean("productService");
    }

    @Test
    public void test_batchCreate() {
        Product productTemplate = new Product();
        productTemplate.setProductBaseId(1);
        List<String> productKeyList = Arrays.asList("zgUQNF_HQV-rWLi1G6bLnx");

        productService.batchCreate(productTemplate, productKeyList);


    }
}

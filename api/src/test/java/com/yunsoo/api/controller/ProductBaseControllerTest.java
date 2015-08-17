package com.yunsoo.api.controller;

import com.yunsoo.api.config.CacheConfiguration;
import com.yunsoo.api.config.DomainConfiguration;
import com.yunsoo.api.dto.ProductBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * Created by admin on 2015/7/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {DomainConfiguration.class, CacheConfiguration.class})

public class ProductBaseControllerTest {

    @Autowired
    private ProductBaseController productBaseController;

    @Test
    public void test_getById() throws IOException {
        String productBaseId = "2kmfazyofsqay3gfu3f";
        ProductBase productBase = productBaseController.getById(productBaseId, null);
        if (productBase != null) {
            System.out.println("getById works");
        }
    }
}

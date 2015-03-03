package com.yunsoo.api;

import com.yunsoo.api.config.DataAPIConfiguration;
import com.yunsoo.api.data.DataAPIClient;
import com.yunsoo.api.dto.ProductStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2015/3/2
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataAPIConfiguration.class})
@WebAppConfiguration
public class DataClientTest {

    @Autowired
    private DataAPIClient dataAPIClient;

    @Test
    public void testGetBaseURL() {
        System.out.println(dataAPIClient.getBaseURL());
    }

    @Test
    public void testGet() {
        ProductStatus productStatus = dataAPIClient.get("productstatus/{id}", ProductStatus.class, 1);
        System.out.println(productStatus);

        ProductStatus[] array = dataAPIClient.get("productstatus?active={active}", ProductStatus[].class, true);
        System.out.println(Arrays.asList(array));
    }

}

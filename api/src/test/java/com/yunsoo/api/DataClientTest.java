package com.yunsoo.api;

import com.yunsoo.api.config.DataAPIConfiguration;
import com.yunsoo.common.data.object.ProductKeyTypeObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.api.dto.ProductStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;

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
    private RestClient dataAPIClient;

    @Test
    public void testGetBaseURL() {
        System.out.println(dataAPIClient.getBaseURL());
    }

    @Test
    public void testGet() {
        ProductKeyTypeObject[] array = dataAPIClient.get("productkeytype?active={active}", ProductKeyTypeObject[].class, true);
        System.out.println(Arrays.asList(array));
    }

}

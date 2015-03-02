package com.yunsoo.api.controller;

import com.yunsoo.api.dto.ProductStatus;
import com.yunsoo.api.util.YunsooConfig;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/2/27
 * Descriptions:
 */
public class ProductStatusControllerTest {

    @Test
    public void tempTestForRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        List<ProductStatus> result = restTemplate.getForObject("http://localhost/api/productstatus/",
                //new ArrayList<ProductStatus>(0).getClass());
                List.class);
        System.out.println(result);
    }

    @Test
    public void tempTestForYunsooConfig(){
        System.out.println(YunsooConfig.getDataAPIBaseUri());
    }
}

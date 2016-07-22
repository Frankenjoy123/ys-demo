package com.yunsoo.data.api.controller;

import com.yunsoo.data.api.ControllerTestBase;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2015/9/17
 * Descriptions:
 */
public class DebugControllerTest extends ControllerTestBase {

    @Test
    public void test_info() {
        Map<String, Object> info = dataApiClient.get("debug", new ParameterizedTypeReference<Map<String, Object>>() {
        });
        System.out.println(info);

    }

}

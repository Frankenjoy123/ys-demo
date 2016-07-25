package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.config.ClientConfiguration;
import com.yunsoo.common.web.client.RestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ClientConfiguration.class})
@EnableAutoConfiguration
public class UserControllerTest {

    @Autowired
    private RestClient dataApiClient;

    UserController userController = new UserController();

    @Test
    public void testGetById() throws Exception {

    }

    @Test
    public void testGetByCellular() throws Exception {

    }

    @Test
    public void testGetByDevicecode() throws Exception {

    }

    @Test
    public void testGetThumbnail() throws Exception {

    }

    @Test
    public void testUpdateUser() throws Exception {

    }

    @Test
    public void testDeleteUser() throws Exception {

    }
}
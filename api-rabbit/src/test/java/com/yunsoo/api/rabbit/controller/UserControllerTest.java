package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.config.ClientConfiguration;
import com.yunsoo.api.rabbit.dto.User;
import com.yunsoo.common.web.client.RestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ClientConfiguration.class})
@EnableAutoConfiguration
public class UserControllerTest {

    @Autowired
    private RestClient dataAPIClient;

    UserController userController = new UserController();

    @Test
    public void testGetById() throws Exception {
        System.out.println(dataAPIClient.getBaseURL());

        User user = userController.getById("552ddb49e6b1e79c80c950b7");
        assertNotNull(user);

        User userNull = userController.getById("abd889");
        assertNull(userNull);
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
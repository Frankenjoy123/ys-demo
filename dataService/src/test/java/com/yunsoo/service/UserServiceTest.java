package com.yunsoo.service;

import com.yunsoo.dao.util.SpringDaoUtil;
import com.yunsoo.service.UserService;
import com.yunsoo.service.contract.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import static org.junit.Assert.*;

public class UserServiceTest {

    private ApplicationContext applicationContext;
    private UserService userService;

    @Before
    public void setUp() throws Exception {
//        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        applicationContext = SpringDaoUtil.getApplicationContext();
        userService = (UserService) applicationContext
                .getBean("userService");
    }

    @Test
    public void testGet() throws Exception {
        User user = userService.get((long) 1);
        System.out.println("user name: " + user.getName());
        assertNotNull(user);
    }

    @Test
    public void testSave() throws Exception {

    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<User> userList = userService.getAllUsers();
        for (User user : userList) {
            System.out.println("user name: " + user.getName());
        }
        assertNotNull(userList);
    }
}
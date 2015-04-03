package com.yunsoo.service;

import com.yunsoo.service.contract.LogisticsCheckAction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml.bc"})
public class LogisticsCheckActionServiceTest {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private LogisticsCheckActionService actionService;

    @Before
    public void setUp() throws Exception {
//        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml.bc");
//        applicationContext = SpringDaoUtil.getApplicationContext();
//        userService = (UserService) applicationContext
//                .getBean("userService");
    }

    @Test
    public void testGet() throws Exception {
        LogisticsCheckAction action = actionService.get(1);
        System.out.println("action name: " + action.getName());
        assertNotNull(action);
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
        List<LogisticsCheckAction> actionList = actionService.getAllLogisticsCheckActions();
        for (LogisticsCheckAction action : actionList) {
            System.out.println("action name: " + action.getName());
        }
        assertNotNull(actionList);
    }
}
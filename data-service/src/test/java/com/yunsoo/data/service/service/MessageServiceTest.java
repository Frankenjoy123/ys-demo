package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.MessageService;
import com.yunsoo.data.service.service.contract.Message;
import org.joda.time.DateTime;
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
public class MessageServiceTest {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private MessageService messageService;

    @Before
    public void setUp() throws Exception {
//        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml.bc");
//        applicationContext = SpringDaoUtil.getApplicationContext();
//        messageService = (MessageService) applicationContext
//                .getBean("messageService");

    }

    @Test
    public void testGet() throws Exception {
//        System.out.println("StatusId: "+ YunsooConfig.getMessageDeleteStatus());
        Message message = messageService.get(1L);
        assertNotNull(message);
    }

    @Test
    public void testSave() throws Exception {
        Message newMessage = new Message();
        newMessage.setTitle("测试新消息-标题");
        newMessage.setBody("主要内容，测试， Body，test 测试吧");
        newMessage.setLink("www.google.com");
        newMessage.setCompanyId(4);
        newMessage.setCreatedBy(10);
        newMessage.setCreatedDateTime(DateTime.now().toString());
        newMessage.setExpireDateTime(DateTime.now().plusDays(7).toString());
        newMessage.setType(1);

        messageService.save(newMessage);
    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testGetMessagesByStatus() throws Exception {
        List<Message> messageList = messageService.getMessagesByStatus(1);
        if (messageList.size() == 0) {
            System.out.println("testGetMessagesByStatus - message is zero");
        }
        for (Message message : messageList) {
            System.out.println("message name: " + message.getTitle());
        }
        assertNotNull(messageList);
    }

    @Test
    public void testGetMessagesByType() throws Exception {

    }

    @Test
    public void testGetMessagesByFilter() throws Exception {
        List<Message> messageList = messageService.getMessagesByFilter(2, 2, 24L, true);
        if (messageList.size() == 0) {
            System.out.println("message is zero");
        }
        for (Message message : messageList) {
            System.out.println("message name: " + message.getTitle() + "  " + message.getCreatedDateTime());
        }
        assertNotNull(messageList);
    }

    @Test
    public void testGetUnreadMessages() throws Exception {
        List<Message> messageList = messageService.getUnreadMessages(1L, 1L);
        if (messageList.size() == 0) {
            System.out.println("unread message is zero");
        }
        for (Message message : messageList) {
            System.out.println("message name: " + message.getTitle() + "  " + message.getCreatedDateTime());
        }
        assertNotNull(messageList);
    }
}
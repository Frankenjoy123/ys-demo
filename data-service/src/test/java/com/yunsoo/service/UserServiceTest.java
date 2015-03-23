package com.yunsoo.service;

import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.service.contract.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml.bc"})
public class UserServiceTest {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {
//        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml.bc");
//        applicationContext = SpringDaoUtil.getApplicationContext();
//        userService = (UserService) applicationContext
//                .getBean("userService");
    }

    @Test
    public void testGet() throws Exception {
        User user = userService.get((long) 1);
        System.out.println("user name: " + user.getName());
        assertNotNull(user);
    }

    @Test
    public void testSave() throws Exception {
        //byte[] demBytes = null; //instead of null, specify your bytes here.
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("yunsoo.properties");
        Path path = Paths.get(url.toURI());
        byte[] demBytes = Files.readAllBytes(path);
//        File outputFile = new File("classpath:yunsoo.properties");
//        try ( FileOutputStream outputStream = new FileOutputStream(outputFile); ) {
//            outputStream.write(demBytes);  //write the bytes and your done.
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        FileObject fileObject = new FileObject();
        fileObject.setSuffix("jpg");
        fileObject.setThumbnailData(demBytes);

        User user = new User();
        user.setName("宁波JY");
        user.setDeviceCode("65-KDJ=DSDl-LKJNN-NOLDK");
        user.setCellular("67548093");
        user.setAddress("宁波大学**寝室");
        user.setFileObject(fileObject);

        long uID = userService.save(user);
        assertTrue(uID > 0L);
    }

    @Test
    public void testUpdate() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("amazon.properties");
        Path path = Paths.get(url.toURI());
        byte[] demBytes = Files.readAllBytes(path);

        FileObject fileObject = new FileObject();
        fileObject.setSuffix("png");
        fileObject.setThumbnailData(demBytes);

        User user = userService.get(38L);
        user.setFileObject(fileObject);
        user.setAddress("Hangzhou 中山路99号1nong");
        ServiceOperationStatus serviceOperationStatus = userService.update(user);
        assertTrue(serviceOperationStatus == ServiceOperationStatus.Success);
    }

    @Test
    public void testPatchUpdate() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("amazon.properties");
        Path path = Paths.get(url.toURI());
        byte[] demBytes = Files.readAllBytes(path);

        FileObject fileObject = new FileObject();
        fileObject.setSuffix("png");
        fileObject.setThumbnailData(demBytes);

        User user = new User();
        user.setId("39");
        user.setFileObject(fileObject);
        user.setAddress("Shanghai - 中山路99号");
        ServiceOperationStatus serviceOperationStatus = userService.patchUpdate(user);
        assertTrue(serviceOperationStatus == ServiceOperationStatus.Success);

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
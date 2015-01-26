package com.yunsoo.service;

import java.util.List;

import com.yunsoo.service.contract.ProductCategory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
//import static org.junit.Assert.*;

/*
* Unit test for ProductCategoryService
* Created By Zhe Zhang. 2015/1/26
* */
@RunWith(SpringJUnit4ClassRunner.class)
//@Configuration
//@EnableAutoConfiguration
@ComponentScan
@ContextConfiguration(locations = {"classpath:**/applicationContext.xml"})
public class ProductCategoryServiceTest {

    @Autowired
    private ApplicationContext applicationContext;
    //    @Autowired
    private ProductCategoryService productCategoryService;

    @Before
    public void setUp() {
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
//        productCategoryService = new ProductCategoryServiceImpl();
        productCategoryService = (ProductCategoryService) applicationContext
                .getBean("productCategoryService");
    }

//    @After
//    public void tearDown() throws Exception {
//
//    }

    @Test
    public void testGetById() throws Exception {
        int id = -1; //not exist id
        ProductCategory productCategory = productCategoryService.getById(id);
        assertNull(productCategory);
        ProductCategory productCategoryExist = productCategoryService.getById(2);
        assertNotNull(productCategoryExist);
    }

    @Test
    public void testSave() throws Exception {
//        ProductCategory productCategory =  productCategoryService.getById(36);
//        assertNotNull(productCategory);
//        productCategory.setDescription("测试存储");
//        productCategoryService.save(productCategory);
//        ProductCategory productCategoryAfterUpdate =  productCategoryService.getById(36);
//        assertEquals(productCategoryAfterUpdate.getDescription(), "测试存储");
    }

    @Test
    public void testUpdate() throws Exception {
        String toTest = "测试存储";
        ProductCategory productCategory = productCategoryService.getById(36);
        assertNotNull(productCategory);
        productCategory.setDescription(toTest);
        productCategoryService.update(productCategory);
        ProductCategory productCategoryAfterUpdate = productCategoryService.getById(36);
        assertEquals(productCategoryAfterUpdate.getDescription(), toTest);
    }

    @Test
    public void testDelete() throws Exception {
        int idToDelete = 37;
        ProductCategory productCategory = productCategoryService.getById(idToDelete);
        assertNotNull(productCategory);
        productCategoryService.delete(productCategory);
        productCategory = productCategoryService.getById(idToDelete);
        assertNull(productCategory);
    }

    @Test
    public void testGetAllProductCategories() throws Exception {
        List<ProductCategory> productCategoryList = productCategoryService.getAllProductCategories();
        for (ProductCategory productCategory : productCategoryList) {
            System.out.println("productCategory name: " + productCategory.getName());
        }
        assertNotNull(productCategoryList);
    }

    @Test
    public void testGetProductCategoriesByParentId() throws Exception {

        List<ProductCategory> productCategoryList = productCategoryService.getProductCategoriesByParentId(1);
        for (ProductCategory productCategory : productCategoryList) {
            System.out.println("productCategory name: " + productCategory.getName());
        }
        assertNotNull(productCategoryList);
    }

    @Test
    public void testGetRootProductCategories() throws Exception {

        List<ProductCategory> productCategoryList = productCategoryService.getRootProductCategories();
        for (ProductCategory productCategory : productCategoryList) {
            System.out.println("productCategory name: " + productCategory.getName());
        }
        assertNotNull(productCategoryList);
    }
}
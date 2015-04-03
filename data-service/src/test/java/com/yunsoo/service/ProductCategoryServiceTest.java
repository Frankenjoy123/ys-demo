package com.yunsoo.service;

import java.util.List;

import com.yunsoo.service.contract.ProductCategory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
//@ContextConfiguration
//@ComponentScan
@ContextConfiguration(locations = {"classpath:applicationContext.xml.bc"})
//@SpringApplicationConfiguration(classes = ProductCategoryServiceImpl.class)
public class ProductCategoryServiceTest {

//    @Configuration
//    static class Config {
//        // this bean will be injected into the ProductCategoryServiceTest class
//        @Bean
//        public ProductCategoryDao productCategoryDao() {
//            ProductCategoryDao productCategoryDao = new ProductCategoryDaoImpl();
//            return  productCategoryDao;
//        }
//        @Bean
//        public ProductCategoryService productCategoryService() {
//            ProductCategoryService productCategoryService1 = new ProductCategoryServiceImpl();
//            // set properties, etc.
//            return productCategoryService1;
//        }
//    }

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ProductCategoryService productCategoryService;

    @Before
    public void setUp() {
//        applicationContext = SpringDaoUtil.getApplicationContext();
//        productCategoryService = (ProductCategoryService) applicationContext
//                .getBean("productCategoryService");
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
//        int idToDelete = 37;
//        ProductCategory productCategory = productCategoryService.getById(idToDelete);
//        assertNotNull(productCategory);
//        productCategoryService.deletePermanantly(productCategory);
//        productCategory = productCategoryService.getById(idToDelete);
//        assertNull(productCategory);
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
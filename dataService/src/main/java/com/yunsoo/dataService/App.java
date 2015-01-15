package com.yunsoo.dataService;

//import java.io.*;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
//import org.springframework.beans.factory.BeanFactory;  
//import org.springframework.beans.factory.xml.XmlBeanFactory;  
//import org.springframework.core.io.ClassPathResource;  
//import org.springframework.core.io.Resource;  
import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationEvent;
import com.yunsoo.dao.util.*;
import com.yunsoo.dbmodel.*;
import com.yunsoo.hibernate.HibernateSessionManager;
import com.yunsoo.service.*;

/**
 * Run app!
 */
public class App {
    private ProductCategoryService productCategoryService;

    public static void main(String[] args) {
        System.out.println("Start running App.Main()...");
//        try {
//            MemcachedTest.setSampleData();
//        }
//        catch (Exception ex) {
//            System.out.println(ex.getMessage());
//            System.out.println(ex.getStackTrace().toString());
//        }
        ApplicationContext applicationContext = SpringDaoUtil
                .getApplicationContext();

        System.out.println("I am here - 编码问题");
        //TestProductKeyStatus(applicationContext);
        TestProductKeyType(applicationContext);
        //TestProductCategory(applicationContext);
        //TestBaseProduct(applicationContext);
        /*TestEmployee(applicationContext);
        TestProduct(applicationContext);
		TestProductKey(applicationContext);*/

        // CreateProductKey();
        System.out.println("Done!");
    }

    public static void TestBaseProduct(ApplicationContext applicationContext) {
        BaseProductService baseProductService = (BaseProductService) applicationContext
                .getBean("baseProductService");
        System.out.println("BaseProduct List : "
                + baseProductService.getAllProducts());

        BaseProduct baseProd = new BaseProduct();
        // prodKey.setId(0);
        baseProd.setBarcode("BAR88CODE");
        baseProd.setName("COFFEE @CN BRAND");
        baseProd.setDescription("Des TBD");
        baseProd.setDetails("This is Coffee!");
        baseProd.setManufacturerId(20);
        baseProd.setShelfLife(11);
        baseProd.setShelfLifeInterval("PH1");
        baseProd.setSubCategoryId(1);
        baseProd.setCreatedDateTime(new Date());
        baseProductService.save(baseProd);
    }

    public static void TestProduct(ApplicationContext applicationContext) {
        ProductService productService = (ProductService) applicationContext
                .getBean("productService");
        System.out.println("Product List : " + productService.getAllProducts());

        Product prod = new Product();
        // prodKey.setId(0);
        prod.setProductStatusId(12);
        prod.setBaseProductId(3);
        prod.setManufacturingDate(new Date());
        prod.setCreatedDateTime(new Date());
        productService.save(prod);
    }

    public static void TestEmployee(ApplicationContext applicationContext) {
        EmployeeService employeeService = (EmployeeService) applicationContext
                .getBean("employeeService");
        System.out.println("Emp List : " + employeeService.getAllEmployees());
    }

    public static void TestProductKey(ApplicationContext applicationContext) {
        ProductKeyService productKeyService = (ProductKeyService) applicationContext
                .getBean("productKeyService");
        ProductKey prodKey = new ProductKey();
        // prodKey.setId(0);
        prodKey.setKey("DTEST8932");
        prodKey.setProductId(25434L);
        prodKey.setCreatedAccountId(45);
        prodKey.setCreatedClientId(19);
        prodKey.setkeyStatusId(3);
        prodKey.setkeyTypeId(7);
        prodKey.setCreatedDateTime(new Date());

        productKeyService.save(prodKey);
        ProductKey theKey = productKeyService.getById(1);
        System.out.println("The ProductKey is: " + theKey.getKey());

        prodKey.setkeyStatusId(10);
        productKeyService.update(prodKey);
    }

    public static void CreateProductKey() {
        Session session = HibernateSessionManager.getSessionFactory()
                .openSession();
        session.beginTransaction();

        ProductKey prodKey = new ProductKey();
        // prodKey.setId(0);
        prodKey.setKey("ABS234S");
        prodKey.setProductId(23434L);
        prodKey.setCreatedAccountId(1);
        prodKey.setCreatedClientId(89);
        prodKey.setkeyStatusId(3);
        prodKey.setkeyTypeId(5);
        prodKey.setCreatedDateTime(new Date());
        session.save(prodKey);
        session.getTransaction().commit();
    }

    public static void TestProductCategory(ApplicationContext applicationContext) {
        ProductCategoryService productCategoryService = (ProductCategoryService) applicationContext
                .getBean("productCategoryService");

        List<ProductCategory> productCategoryList = productCategoryService.getRootProductCategories();
        for (int i = 0; i < productCategoryList.size(); i++) {
            System.out.println("Product Category (" + i + ") : " + productCategoryList.get(i).getName());
            List<ProductCategory> productCategoryList2 = productCategoryService.getProductCategoriesByParentId(productCategoryList.get(i).getId());
            for (int j = 0; j < productCategoryList2.size(); j++) {
                System.out.println("Sub Product Category (" + j + ") : " + productCategoryList2.get(j).getName());
            }
        }

//        ProductCategory productCategory = new ProductCategory();
//        productCategory.setDescription("\u4E2D\u6587");
//        productCategory.setName("其他");
//        productCategory.setActive(true);
//        productCategoryService.save(productCategory);
    }

    public static void TestProductKeyStatus(ApplicationContext applicationContext) {
        ProductKeyStatusService productKeyStatusService = (ProductKeyStatusService) applicationContext
                .getBean("productKeyStatusService");

        List<ProductKeyStatus> productKeyStatusList = productKeyStatusService.getAllProductKeyStatus(false);
        System.out.println("productKeyStatusList Size: " + productKeyStatusList.size());
        for (int i = 0; i < productKeyStatusList.size(); i++) {
            System.out.println("product Key Status List (" + i + ") : " + productKeyStatusList.get(i).getDescription());
        }
    }

    public static void TestProductKeyType(ApplicationContext applicationContext) {
        System.out.println("I am here - 编码问题");
        ProductKeyTypeService productKeyTypeService = (ProductKeyTypeService) applicationContext
                .getBean("productKeyTypeService");

       /* List<ProductKeyType> productKeyTypeList = productKeyTypeService.getAllProductKeyTypes(false);
        System.out.println("productKeyStatusList Size: " + productKeyTypeList.size());
        for (int i = 0; i < productKeyTypeList.size(); i++) {
            System.out.println("product Key Type List (" + i + ") : " + productKeyTypeList.get(i).getDescription());
        }*/

        ProductKeyType productKeyType = new ProductKeyType();
        productKeyType.setCode("2");
        productKeyType.setDescription("编码问题");
        productKeyTypeService.save(productKeyType);
    }
}

package com.yunsoo.dataService;

import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import com.yunsoo.dao.util.SpringDaoUtil;
import com.yunsoo.dbmodel.*;
import com.yunsoo.service.*;

/**
 * Run app!
 */
@Configuration
@EnableAutoConfiguration
public class App {

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
//       TestBaseProductRepo(applicationContext);
        TestProductKeyStatus(applicationContext);
//        TestProductKeyType(applicationContext);
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
        System.out.println("BaseProductModel List : "
                + baseProductService.getAllProducts());

        BaseProductModel baseProd = new BaseProductModel();
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

//    public static void TestProduct(ApplicationContext applicationContext) {
//        ProductService productService = (ProductService) applicationContext
//                .getBean("productService");
//        System.out.println("ProductModel List : " + productService.getAllProducts());
//
//        ProductModel prod = new ProductModel();
//        // prodKey.setId(0);
//        prod.setProductStatusId(12);
//        prod.setBaseProductId(3);
//        prod.setManufacturingDate(new Date());
//        prod.setCreatedDateTime(new Date());
//        productService.save(prod);
//    }

    public static void TestEmployee(ApplicationContext applicationContext) {
        EmployeeService employeeService = (EmployeeService) applicationContext
                .getBean("employeeService");
        System.out.println("Emp List : " + employeeService.getAllEmployees());
    }

//    public static void TestProductKey(ApplicationContext applicationContext) {
//        ProductKeyService productKeyService = (ProductKeyService) applicationContext
//                .getBean("productKeyService");
//        ProductKeyModel prodKey = new ProductKeyModel();
//        // prodKey.setId(0);
//        prodKey.setKey("DTEST8932");
//        prodKey.setProductId(25434L);
//        prodKey.setCreatedAccountId(45);
//        prodKey.setCreatedClientId(19);
//        prodKey.setkeyStatusId(3);
//        prodKey.setkeyTypeId(7);
//        prodKey.setCreatedDateTime(new Date());
//
//        productKeyService.save(prodKey);
//        ProductKeyModel theKey = productKeyService.getById(1);
//        System.out.println("The ProductKeyModel is: " + theKey.getKey());
//
//        prodKey.setkeyStatusId(10);
//        productKeyService.update(prodKey);
//    }

    public static void TestProductCategory(ApplicationContext applicationContext) {
        ProductCategoryService productCategoryService = (ProductCategoryService) applicationContext
                .getBean("productCategoryService");

        List<com.yunsoo.service.contract.ProductCategory> productCategoryModelList = productCategoryService.getRootProductCategories();
        for (int i = 0; i < productCategoryModelList.size(); i++) {
            System.out.println("ProductModel Category (" + i + ") : " + productCategoryModelList.get(i).getName());
            List<com.yunsoo.service.contract.ProductCategory> productCategoryModelList2 = productCategoryService.getProductCategoriesByParentId(productCategoryModelList.get(i).getId());
            for (int j = 0; j < productCategoryModelList2.size(); j++) {
                System.out.println("Sub ProductModel Category (" + j + ") : " + productCategoryModelList2.get(j).getName());
            }
        }

//        ProductCategoryModel productCategory = new ProductCategoryModel();
//        productCategory.setDescription("\u4E2D\u6587");
//        productCategory.setName("其他");
//        productCategory.setActive(true);
//        productCategoryService.save(productCategory);
    }

    public static void TestProductKeyStatus(ApplicationContext applicationContext) {
        ProductKeyStatusService productKeyStatusService = (ProductKeyStatusService) applicationContext
                .getBean("productKeyStatusService");

        List<ProductKeyStatusModel> productKeyStatusModelList = productKeyStatusService.getAllProductKeyStatus(false);
        System.out.println("productKeyStatusModelList Size: " + productKeyStatusModelList.size());
        for (int i = 0; i < productKeyStatusModelList.size(); i++) {
            System.out.println("product Key Status List (" + i + ") : " + productKeyStatusModelList.get(i).getDescription());
        }

        ProductKeyStatusModel productKeyStatusModel = new ProductKeyStatusModel();
        productKeyStatusModel.setDescription("码问测试中文存储1");
        productKeyStatusModel.setCode("MyCode1");

        productKeyStatusService.save(productKeyStatusModel);
    }

    public static void TestProductKeyType(ApplicationContext applicationContext) {
        System.out.println("I am here - 编码问题");
        ProductKeyTypeService productKeyTypeService = (ProductKeyTypeService) applicationContext
                .getBean("productKeyTypeService");

       /* List<ProductKeyTypeModel> productKeyTypeList = productKeyTypeService.getAllProductKeyTypes(false);
        System.out.println("productKeyStatusList Size: " + productKeyTypeList.size());
        for (int i = 0; i < productKeyTypeList.size(); i++) {
            System.out.println("product Key Type List (" + i + ") : " + productKeyTypeList.get(i).getDescription());
        }*/

        ProductKeyTypeModel productKeyTypeModel = new ProductKeyTypeModel();
        productKeyTypeModel.setCode("2");
        productKeyTypeModel.setDescription("编码问题");
        productKeyTypeService.save(productKeyTypeModel);
    }

}

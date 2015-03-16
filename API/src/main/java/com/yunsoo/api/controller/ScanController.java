package com.yunsoo.api.controller;

import com.yunsoo.api.biz.validateProduct;
import com.yunsoo.api.dto.ScanResult;
import com.yunsoo.api.dto.basic.*;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Zhe on 2015/2/27.
 */
@RestController
@RequestMapping("/scan")
public class ScanController {

    private RestClient dataAPIClient;

    @Autowired
    ScanController(RestClient dataAPIClient) {
        this.dataAPIClient = dataAPIClient;
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public ScanResult getDetailByKey(@PathVariable(value = "key") String key,
                                     @RequestParam(value = "userId", required = false) Integer userId,
                                     @RequestParam(value = "device", required = false) String deviceCode) {

        //1, get user
        User currentUser = this.getUser(userId, deviceCode);
        if (currentUser == null)
            throw new NotFoundException("User not found by userId = " + userId + " deviceCode = " + deviceCode);

        ScanResult scanResult = new ScanResult();
        scanResult.setKey(key);

        //2, retrieve scan records
        ScanRecord[] scanRecords = dataAPIClient.get("scan/filterby?productKey={productKey}", ScanRecord[].class, key);
        List<ScanRecord> scanRecordList = Arrays.asList(scanRecords == null ? new ScanRecord[0] : scanRecords);
        scanResult.setScanRecord(scanRecordList);

        //3, set product information
        scanResult.setProduct(this.getProductByKey(key));

        //4, retrieve logistics information
        LogisticsCheckPath[] logisticsCheckPath = dataAPIClient.get("logisticscheckpath/key/{key}", LogisticsCheckPath[].class, key);
        Logistics logistics = new Logistics();
//        logistics.setLocation(logisticsCheckPath.getStartCheckPoint().toString());
//        logistics.setDateTime(logisticsCheckPath.getStartDate());
        scanResult.setLogisticsList(this.getFakeLogistics());

        //5, get company information.
        System.out.print(" OrgId: " + scanResult.getProduct().getManufacturerId());
        Organization organization = dataAPIClient.get("organization/id/{id}", Organization.class, scanResult.getProduct().getManufacturerId());
        scanResult.setManufacturer(organization);

        //6, set validation result by our validation strategy.
        scanResult.setValidationResult(validateProduct.validateProduct(scanResult.getProduct(), currentUser, scanRecordList));

        //7, save scan Record
        ScanRecord scanRecord = new ScanRecord();
        scanRecord.setUserId(currentUser.getId());
        scanRecord.setDeviceId(currentUser.getDeviceCode());
        scanRecord.setClientId(123456);
        scanRecord.setProductKey(key);
        scanRecord.setBaseProductId(scanResult.getProduct().getProductBaseId());
        scanRecord.setDetail(currentUser.getName() + "扫描验证真伪。");
        return scanResult;
    }

    //Retrieve Product Key, ProductBase entry and Product-Category entry from Backend.
    private Product getProductByKey(String Key) {
        Product product = new Product();
        product.setProductKey(Key);

        ProductObject productObject = dataAPIClient.get("product/{Key}", ProductObject.class, Key);
        if (productObject == null) {
            //to-do: log ...该产品码对应的产品不存在！
        } else {
            product.setStatusId(productObject.getProductStatusId());
            if (productObject.getManufacturingDateTime() != null) {
                product.setManufacturingDateTime(productObject.getManufacturingDateTime().toString());
            }
            product.setCreatedDateTime(productObject.getCreatedDateTime().toString());

            //fill with ProductBase information.
            int productBaseId = productObject.getProductBaseId();
            ProductBaseObject productBaseObject = dataAPIClient.get("productbase/{id}", ProductBaseObject.class, productBaseId);
            product.setProductBaseId(productBaseId);
            product.setBarcode(productBaseObject.getBarcode());
            product.setDescription(productBaseObject.getDescription());
            product.setDetails(productBaseObject.getDetails());
            product.setName(productBaseObject.getName());
            product.setManufacturerId(productBaseObject.getManufacturerId());

            //fill with ProductCategory information.
            ProductCategory productCategory = dataAPIClient.get("productcategory/model?id={id}", ProductCategory.class, productBaseObject.getSubCategoryId());
            product.setProductCategory(productCategory);
        }
        return product;
    }

    private ProductCategory getFakeProductCategory() {
        ProductCategory category = new ProductCategory();
        category.setName("水");
        category.setActive(true);
        category.setDescription("水啊水");
        category.setId(10);
        category.setParentId(2);
        return category;
    }

    private List<Logistics> getFakeLogistics() {
        List<Logistics> logisticsList = new ArrayList<Logistics>();
        Logistics logistics1 = new Logistics();
        logistics1.setOrgId(1);
        logistics1.setOrgName("顺丰快递");
        logistics1.setMessage("从广州发往杭州");
        logistics1.setLocation("广州番禺XX街88号");
        logistics1.setDateTime("2015-02-23");
        logisticsList.add(logistics1);

        Logistics logistics2 = new Logistics();
        logistics2.setOrgId(1);
        logistics2.setOrgName("杭州工商检验");
        logistics2.setMessage("杭州下沙检验所检验合格");
        logistics2.setLocation("杭州下沙XX街88号");
        logistics2.setDateTime("2015-02-25");
        logisticsList.add(logistics2);
        return logisticsList;
    }

    //call dataAPI to get current User
    private User getUser(Integer userId, String deviceCode) {
        User user = null;
        if (userId != null && userId > 0) {
            user = dataAPIClient.get("user/id/{id}", User.class, userId);
        } else {
            user = dataAPIClient.get("user/token/{devicecode}", User.class, deviceCode);
        }
        return user;
    }
}

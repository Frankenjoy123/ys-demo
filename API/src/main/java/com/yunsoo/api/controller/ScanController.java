package com.yunsoo.api.controller;

import com.yunsoo.api.biz.validateProduct;
import com.yunsoo.api.dto.ScanResult;
import com.yunsoo.api.dto.basic.*;
//import org.joda.time.DateTime;
import com.yunsoo.api.object.TProduct;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
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

        User currentUser = this.getUser(userId, deviceCode);
        if (currentUser == null)
            throw new NotFoundException("User not found by userId = " + userId + " deviceCode = " + deviceCode);

        ScanResult scanResult = new ScanResult();
        scanResult.setKey(key);
        List<ScanRecord> scanRecords = dataAPIClient.get("scan/filterby?productKey={productKey}", List.class, key);
        scanResult.setScanRecord(scanRecords);

        scanResult.setLogisticsList(this.getFakeLogistics());
        scanResult.setManufacturer(null);
        scanResult.setProduct(this.getProductByKey(key));

        scanResult.setValidationResult(validateProduct.validateProduct(scanResult.getProduct(), currentUser, scanRecords));

        return scanResult;
    }

    private Product getProductByKey(String Key) {
        Product product = new Product();
        product.setProductKey(Key);

        product.setCreatedDateTime("2015-02-02");
        product.setBarcode("KJ:LDS");
        product.setDescription("这是一瓶农夫山泉");
        product.setDetails("各种详细介绍..");
        product.setName("农夫山泉");
        product.setStatusId(1);
        product.setManufacturerId(1);
        product.setManufacturingDateTime("2015-02-02");
        product.setProductCategory(this.getFakeProductCategory());

        TProduct tProduct = dataAPIClient.get("product/{Key}", TProduct.class, Key);
        product.setStatusId(tProduct.getProductStatusId());
        product.setManufacturingDateTime(tProduct.getManufacturingDateTime());
        product.setCreatedDateTime(tProduct.getCreatedDateTime());
        int productBaseId = tProduct.getProductBaseId();

        //To-do: fill with ProductBase information.

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

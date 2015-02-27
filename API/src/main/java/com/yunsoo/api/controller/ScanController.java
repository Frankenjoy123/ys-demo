package com.yunsoo.api.controller;

import com.yunsoo.api.dto.ScanResult;
import com.yunsoo.api.dto.basic.Logistics;
import com.yunsoo.api.dto.basic.Product;
import com.yunsoo.api.dto.basic.ProductCategory;
import com.yunsoo.api.exception.UnauthorizedException;
//import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe on 2015/2/27.
 */
@RestController
@RequestMapping("/scan")
public class ScanController {

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public ResponseEntity<?> getDetailByKey(@PathVariable(value = "key") String key) {
        ScanResult ScanResult = new ScanResult();
        ScanResult.setKeyType("new key");
        ScanResult.setLogisticsList(this.getFakeLogistics());
        ScanResult.setManufacturer(null);
        ScanResult.setProduct(this.getFakeProduct());

//        List<ScanRecord> scanRecordList = scanRecordService.getScanRecordsByFilter(key, null, null, null,
//                0, 100);
//        ScanResult.setScanRecord(ScanRecordDto.FromScanRecordList(scanRecordList));
        return new ResponseEntity(ScanResult, HttpStatus.OK);
    }

    @RequestMapping(value = "/forbidden", method = RequestMethod.GET)
    public ResponseEntity<?> forbidden() {
        throw new UnauthorizedException("JB_USER_909");
    }

    private Product getFakeProduct() {
        Product product = new Product();
        product.setProductKey("ABKLJLKOIED9823klLDSD");
        product.setCreatedDateTime("2015-02-02");
        product.setBarcode("KJ:LDS");
        product.setDescription("这是一瓶农夫山泉");
        product.setDetails("各种详细介绍..");
        product.setName("农夫山泉");
        product.setStatusId(1);
        product.setManufacturerId(1);
        product.setManufacturingDateTime("2015-02-02");
        product.setProductCategory(this.getFakeProductCategory());
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

}

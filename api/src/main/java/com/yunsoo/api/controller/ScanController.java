package com.yunsoo.api.controller;

import com.yunsoo.api.biz.LogisticsDomain;
import com.yunsoo.api.domain.ProductDomain;
import com.yunsoo.api.biz.UserDomain;
import com.yunsoo.api.biz.ValidateProduct;
import com.yunsoo.api.dto.LogisticsPath;
import com.yunsoo.api.dto.ScanResult;
import com.yunsoo.api.dto.basic.*;
import com.yunsoo.api.object.ValidationResult;
import com.yunsoo.common.util.DateTimeUtils;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/2/27
 * Descriptions:
 */
@RestController
@RequestMapping("/scan")
public class ScanController {

    @Autowired
    private RestClient dataAPIClient;
    @Autowired
    private LogisticsDomain logisticsDomain;
    @Autowired
    private UserDomain userDomain;
    @Autowired
    private ProductDomain productDomain;


    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public ScanResult getDetailByKey(@PathVariable(value = "key") String key,
                                     @RequestParam(value = "userId", required = false) Integer userId,
                                     @RequestParam(value = "device", required = false) String deviceCode) {

        //1, get user
        User currentUser = userDomain.getUser(userId, deviceCode);
        if (currentUser == null) {
            throw new NotFoundException("User not found by userId = " + userId + " deviceCode = " + deviceCode);
        }

        ScanResult scanResult = new ScanResult();
        scanResult.setKey(key);

        //2, set product information
        Product currentExistProduct = productDomain.getProductByKey(key);
        if (currentExistProduct == null) {
            //Not found by the product Key
            scanResult.setValidationResult(ValidationResult.Fake);  //no such key in our Yunsoo Platform.
            return scanResult;
        }
        scanResult.setProduct(currentExistProduct);

        //3, retrieve scan records
        ScanRecord[] scanRecords = dataAPIClient.get("scan/filterby?productKey={productKey}", ScanRecord[].class, key);
        List<ScanRecord> scanRecordList = Arrays.asList(scanRecords == null ? new ScanRecord[0] : scanRecords);
        scanResult.setScanRecord(scanRecordList);
        scanResult.setScanCounter(scanRecordList.size() + 1); //设置当前是第几次被最终用户扫描 - 根据用户扫描记录表.

        //4, retrieve logistics information
        scanResult.setLogisticses(ConvertToLogisticsDigest(logisticsDomain.getLogisticsPathsOrderByStartDate(key)));

        //5, get company information.
        Organization organization = dataAPIClient.get("organization/id/{id}", Organization.class, scanResult.getProduct().getManufacturerId());
        scanResult.setManufacturer(organization);

        //6, set validation result by our validation strategy.
        scanResult.setValidationResult(ValidateProduct.validateProduct(scanResult.getProduct(), currentUser, scanRecordList));

        //7, save scan Record
        long scanSave = SaveScanRecord(currentUser, currentExistProduct);
        return scanResult;
    }

    @RequestMapping(value = "/history/user/{userId}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
    public List<ScanRecord> getScanRecordsByFilter(
            @PathVariable(value = "userId") Long userId,
            @PathVariable(value = "pageIndex") Integer pageIndex,
            @PathVariable(value = "pageSize") Integer pageSize) {

        //验证输入参数
        if (userId == null || userId <= 0) {
            throw new BadRequestException("用户ID不应小于0！");
        }
        if (pageIndex == null) {
            pageIndex = 0;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageIndex < 0) {
            throw new BadRequestException("pageIndex不应小于0！");
        }
        if (pageSize <= 0) {
            throw new BadRequestException("PageSize不应小于等于0！");
        }

        ScanRecord[] scanRecords = dataAPIClient.get("scan/filterby?userId={userId}&pageIndex={pageIndex}&pageSize={pageSize}", ScanRecord[].class, userId, pageIndex, pageSize);
        List<ScanRecord> scanRecordList = Arrays.asList(scanRecords == null ? new ScanRecord[0] : scanRecords);
        return scanRecordList;
    }

    private List<Logistics> ConvertToLogisticsDigest(List<LogisticsPath> logisticsPaths) {
        List<Logistics> logisticsList = new ArrayList<Logistics>();
        for (LogisticsPath path : logisticsPaths) {
            Logistics logistics1 = new Logistics();
            logistics1.setOrgId(path.getStartCheckPointObject().getOrgId());
            logistics1.setOrgName(path.getStartCheckPointOrgObject().getName());
            logistics1.setMessage(path.getActionObject().getName());
            logistics1.setLocation(path.getStartCheckPointObject().getName());
            logistics1.setDateTime(DateTimeUtils.toString(path.getStartDate()));
            logisticsList.add(logistics1);
        }
        return logisticsList;
    }

    private long SaveScanRecord(User currentUser, Product currentProduct) {
        ScanRecord scanRecord = new ScanRecord();
        scanRecord.setUserId(currentUser.getId());
        scanRecord.setDeviceId(currentUser.getDeviceCode());
        scanRecord.setClientId(123456);
        scanRecord.setProductKey(currentProduct.getProductKey());
        scanRecord.setBaseProductId(currentProduct.getProductBaseId());
        scanRecord.setDetail(currentUser.getName() + "扫描验证真伪。");

        return dataAPIClient.post("scan/save", scanRecord, Long.class);
    }
}

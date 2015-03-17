package com.yunsoo.api.controller;

import com.yunsoo.api.biz.LogisticsDomain;
import com.yunsoo.api.biz.ProductDomain;
import com.yunsoo.api.biz.UserDomain;
import com.yunsoo.api.biz.validateProduct;
import com.yunsoo.api.dto.LogisticsPath;
import com.yunsoo.api.dto.ScanResult;
import com.yunsoo.api.dto.basic.*;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.util.DateTimeUtils;
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
    private LogisticsDomain logisticsDomain;
    @Autowired
    private UserDomain userDomain;
    @Autowired
    private ProductDomain productDomain;

    @Autowired
    ScanController(RestClient dataAPIClient) {
        this.dataAPIClient = dataAPIClient;
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public ScanResult getDetailByKey(@PathVariable(value = "key") String key,
                                     @RequestParam(value = "userId", required = false) Integer userId,
                                     @RequestParam(value = "device", required = false) String deviceCode) {

        //1, get user
        User currentUser = userDomain.getUser(userId, deviceCode);
        if (currentUser == null)
            throw new NotFoundException("User not found by userId = " + userId + " deviceCode = " + deviceCode);

        ScanResult scanResult = new ScanResult();
        scanResult.setKey(key);

        //2, retrieve scan records
        ScanRecord[] scanRecords = dataAPIClient.get("scan/filterby?productKey={productKey}", ScanRecord[].class, key);
        List<ScanRecord> scanRecordList = Arrays.asList(scanRecords == null ? new ScanRecord[0] : scanRecords);
        scanResult.setScanRecord(scanRecordList);

        //3, set product information
        scanResult.setProduct(productDomain.getProductByKey(key));

        //4, retrieve logistics information
        scanResult.setLogisticses(ConvertToLogisticsDigest(logisticsDomain.getLogisticsPathsOrderByStartDate(key)));

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

    private List<Logistics> ConvertToLogisticsDigest(List<LogisticsPath> logisticsPaths) {
        List<Logistics> logisticsList = new ArrayList<Logistics>();
        for (LogisticsPath path : logisticsPaths) {
            Logistics logistics1 = new Logistics();
            logistics1.setOrgId(path.getStartCheckPointObject().getOrgId());
            logistics1.setOrgName("顺丰快递");
            logistics1.setMessage(path.getActionObject().getShortDesc());
            logistics1.setLocation(path.getStartCheckPointObject().getName());
            logistics1.setDateTime(DateTimeUtils.toString(path.getStartDate()));
            logisticsList.add(logistics1);
        }
//        Logistics logistics1 = new Logistics();
//        logistics1.setOrgId(1);
//        logistics1.setOrgName("顺丰快递");
//        logistics1.setMessage("从广州发往杭州");
//        logistics1.setLocation("广州番禺XX街88号");
//        logistics1.setDateTime("2015-02-23");
//        logisticsList.add(logistics1);
//
//        Logistics logistics2 = new Logistics();
//        logistics2.setOrgId(1);
//        logistics2.setOrgName("杭州工商检验");
//        logistics2.setMessage("杭州下沙检验所检验合格");
//        logistics2.setLocation("杭州下沙XX街88号");
//        logistics2.setDateTime("2015-02-25");
//        logisticsList.add(logistics2);
        return logisticsList;
    }

}

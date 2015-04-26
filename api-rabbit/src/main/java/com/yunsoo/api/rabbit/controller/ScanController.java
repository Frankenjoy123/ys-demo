package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.biz.ValidateProduct;
import com.yunsoo.api.rabbit.domain.LogisticsDomain;
import com.yunsoo.api.rabbit.domain.ProductDomain;
import com.yunsoo.api.rabbit.domain.UserDomain;
import com.yunsoo.api.rabbit.domain.UserFollowDomain;
import com.yunsoo.api.rabbit.dto.LogisticsPath;
import com.yunsoo.api.rabbit.dto.basic.*;
import com.yunsoo.api.rabbit.object.TScanRecord;
import com.yunsoo.api.rabbit.object.ValidationResult;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.util.DateTimeUtils;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/2/27
 * Descriptions: Allow anonymous visit.
 * <p>
 * ErrorCode
 * 40001    :   查询码不能为空
 * 40002    :   查询参数UserId不能为空
 * 40401    :   User not found!
 * 40402    :   ProductKey not found!
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
    private UserFollowDomain userFollowDomain;
    @Autowired
    private ProductDomain productDomain;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScanController.class);

    //能够访问所有的Key
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ScanResult getDetailByKey(@RequestBody ScanRequestBody scanRequestBody) {

        //0，validate input
        scanRequestBody.validateForScan();

        //1, get user
        User currentUser = userDomain.ensureUser(scanRequestBody.getUserId(), scanRequestBody.getDeviceCode(), null);
        if (currentUser == null) {
//            LOGGER.error("User not found by userId ={0}, deviceCode = {1}", scanRequestBody.getUserId(), scanRequestBody.getDeviceCode());
            throw new NotFoundException(40401, "User not found by userId = " + scanRequestBody.getUserId() + " deviceCode = " + scanRequestBody.getDeviceCode());
        }

        ScanResult scanResult = new ScanResult();
        scanResult.setUserId(currentUser.getId());
        scanResult.setKey(scanRequestBody.getKey());

        //2, set product information
        Product currentExistProduct = productDomain.getProductByKey(scanRequestBody.getKey());
        if (currentExistProduct == null) {
            //Not found by the product Key
            LOGGER.warn("Key = {0} 不存在！", scanRequestBody.getKey());
            scanResult.setValidationResult(ValidationResult.Fake);  //no such key in our Yunsoo Platform.
            return scanResult;
        }
        scanResult.setProduct(currentExistProduct);

        //3, retrieve scan records
        ScanRecord[] scanRecords = dataAPIClient.get("scan/filterby?productKey={productKey}&pageSize={pageSize}", ScanRecord[].class, scanRequestBody.getKey(), Integer.MAX_VALUE);
        List<ScanRecord> scanRecordList = Arrays.asList(scanRecords == null ? new ScanRecord[0] : scanRecords);
        //to-do
        scanResult.setScanRecord(scanRecordList);
        scanResult.setScanCounter(scanRecordList.size() + 1); //设置当前是第几次被最终用户扫描 - 根据用户扫描记录表.

        //4, retrieve logistics information
        scanResult.setLogisticsList(getLogisticsInfo(scanRequestBody.getKey()));

        //5, get company information.
        OrganizationObject organizationObject = dataAPIClient.get("organization/{id}", OrganizationObject.class, scanResult.getProduct().getOrgId());
        scanResult.setManufacturer(fromOrganizationObject(organizationObject));

        //6，ensure user following the company
        UserFollowing userFollowing = new UserFollowing();
        userFollowing.setUserId(currentUser.getId());
        userFollowing.setOrganizationId(organizationObject.getId());
        userFollowDomain.ensureFollow(userFollowing);

        //7, set validation result by our validation strategy.
        scanResult.setValidationResult(ValidateProduct.validateProduct(scanResult.getProduct(), currentUser, scanRecordList));

        //8, save scan Record
        long scanSave = SaveScanRecord(currentUser, currentExistProduct, scanRequestBody);
        return scanResult;
    }

    @RequestMapping(value = "/history/user/{userId}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
//    @PreAuthorize("hasPermission(#scanrecord, 'scanrecord:read')")
    public List<ScanRecord> getScanRecordsByFilter(
            @PathVariable(value = "userId") String userId,
            @PathVariable(value = "pageIndex") Integer pageIndex,
            @PathVariable(value = "pageSize") Integer pageSize) {

        //验证输入参数
        if (userId == null || userId.isEmpty()) {
            throw new BadRequestException(40001, "用户ID不应为空！");
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

    @RequestMapping(value = "/searchback/{isbackward}/user/{userId}/from/{Id}/paging/{pageIndex}/{pageSize}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#scanrecord, 'scanrecord:read')")
    public List<ScanRecord> getScanRecordsByFilter(
            @PathVariable(value = "Id") Long Id,
            @PathVariable(value = "userId") String userId,
            @PathVariable(value = "isbackward") Boolean isbackward,
            @PathVariable(value = "pageIndex") Integer pageIndex,
            @PathVariable(value = "pageSize") Integer pageSize) {

        //验证输入参数
        if (userId == null || userId.isEmpty()) {
            throw new BadRequestException(40001, "用户ID不应为空！");
        }
        if (Id == null || Id <= 0) {
            Id = 0L; //default value
        }
        if (isbackward == null) {
            throw new BadRequestException(40001, "isbackward未赋值！");
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

        ScanRecord[] scanRecords = dataAPIClient.get("scan/filter?userId={userId}&Id={Id}&backward={backward}&pageIndex={pageIndex}&pageSize={pageSize}",
                ScanRecord[].class, userId, Id, isbackward, pageIndex, pageSize);
        List<ScanRecord> scanRecordList = Arrays.asList(scanRecords == null ? new ScanRecord[0] : scanRecords);
        return scanRecordList;
    }

    private List<Logistics> getLogisticsInfo(String key) {
        List<LogisticsPath> logisticsPaths;
        try {
            logisticsPaths = logisticsDomain.getLogisticsPathsOrderByStartDate(key);
        } catch (NotFoundException ex) {
            //to do: log
            LOGGER.warn("物流信息找不到 - Key = " + key);
            return null;
        }

        List<Logistics> logisticsList = new ArrayList<Logistics>();
        for (LogisticsPath path : logisticsPaths) {
            Logistics logistics = new Logistics();
            logistics.setOrgId(path.getStartCheckPointObject().getOrgId());
            logistics.setOrgName(path.getStartCheckPointOrgObject().getName());
            logistics.setMessage(path.getActionObject().getName());
            logistics.setLocation(path.getStartCheckPointObject().getName());
            logistics.setDateTime(DateTimeUtils.toString(path.getStartDate()));
            logisticsList.add(logistics);
        }
        return logisticsList;
    }

    private long SaveScanRecord(User currentUser, Product currentProduct, ScanRequestBody scanRequestBody) {
        TScanRecord scanRecord = new TScanRecord();
        scanRecord.setUserId(currentUser.getId());
        scanRecord.setDeviceId(currentUser.getDeviceCode());
        if (scanRequestBody.getAppId() != null && !scanRequestBody.getAppId().isEmpty()) {
            scanRecord.setAppId(scanRequestBody.getAppId()); //记录扫描客户端
        } else {
            scanRecord.setAppId("未知的AppId");
        }
        scanRecord.setProductKey(currentProduct.getProductKey());
        scanRecord.setBaseProductId(currentProduct.getProductBaseId());
        if (scanRequestBody.getDetail() != null && !scanRequestBody.getDetail().isEmpty()) {
            scanRecord.setDetail(scanRequestBody.getDetail()); //接受扫描的相关详情
        } else {
            scanRecord.setDetail("用户扫描验证真伪。");
        }
        scanRecord.setLongitude(scanRequestBody.getLongitude());
        scanRecord.setLatitude(scanRequestBody.getLatitude());
        if (scanRequestBody.getLocation() != null && !scanRequestBody.getLocation().isEmpty()) {
            scanRecord.setLocation(scanRequestBody.getLocation());
        } else {
            scanRecord.setLocation("未公开地址"); //用户选择不公开隐私地址信息
        }
        return dataAPIClient.post("scan", scanRecord, long.class);
    }

    private Organization fromOrganizationObject(OrganizationObject object) {
        Organization entity = new Organization();
        entity.setId(object.getId());
        entity.setName(object.getName());
        entity.setStatusCode(object.getStatusCode());
        entity.setDescription(object.getDescription());
        entity.setTypeCode(object.getTypeCode());
        entity.setDetails(object.getDetails());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        return entity;
    }
}

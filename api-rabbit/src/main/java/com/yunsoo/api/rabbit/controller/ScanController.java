package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.biz.ValidateProduct;
import com.yunsoo.api.rabbit.domain.*;
import com.yunsoo.api.rabbit.dto.LogisticsPath;
import com.yunsoo.api.rabbit.dto.basic.*;
import com.yunsoo.api.rabbit.object.Constants;
import com.yunsoo.api.rabbit.object.TAccount;
import com.yunsoo.api.rabbit.object.ValidationResult;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ScanRecordObject;
import com.yunsoo.common.util.DateTimeUtils;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    @Autowired
    private UserLikedProductDomain userLikedProductDomain;
    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScanController.class);

    //能够访问所有的Key,为移动客户端调用，因此每次Scan都save扫描记录。
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ScanResult getDetail(@RequestHeader(value = Constants.HttpHeaderName.ACCESS_TOKEN, required = false) String accessToken,
                                @RequestBody ScanRequestBody scanRequestBody) {
        //0，validate input
        scanRequestBody.validateForScan();

        //1, get user
        User currentUser = null;
        if (accessToken != null) {
            TAccount tAccount = tokenAuthenticationService.parseUser(accessToken);
            currentUser = userDomain.ensureUser(tAccount.getId(), null, null);
        } else {
            currentUser = dataAPIClient.get("user/{id}", User.class, "2k64dcya672axp5jcgv"); //hardcode for web-scan
        }

        if (currentUser == null) {
//            LOGGER.error("User not found by userId ={0}, deviceCode = {1}", scanRequestBody.getUserId(), scanRequestBody.getDeviceCode());
            throw new NotFoundException(40401, "User not found deviceCode = " + scanRequestBody.getDeviceCode());
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

        //3, set if user liked this product
        UserLikedProduct userLikedProduct = this.userLikedProductDomain.getUserLikedProduct(currentUser.getId(), currentExistProduct.getProductBaseId());
        if (userLikedProduct != null) {
            scanResult.setLiked_product(userLikedProduct.getActive());
        } else {
            scanResult.setLiked_product(false);
        }

        //4, retrieve scan records
        List<ScanRecord> scanRecordList = dataAPIClient.get("scan/filterby?productKey={productKey}&pageSize={pageSize}",
                new ParameterizedTypeReference<List<ScanRecord>>() {
                }, scanRequestBody.getKey(), 20);  //hard code as top 20 (desc order by created time )
        scanResult.setScanRecordList(scanRecordList);
        scanResult.setScanCounter(scanRecordList.size() + 1); //设置当前是第几次被最终用户扫描 - 根据用户扫描记录表.

        //5, retrieve logistics information
        scanResult.setLogisticsList(getLogisticsInfo(scanRequestBody.getKey()));

        //6, get company information.
        OrganizationObject organizationObject = dataAPIClient.get("organization/{id}", OrganizationObject.class, scanResult.getProduct().getOrgId());
        scanResult.setManufacturer(new Organization(organizationObject));

        //7，ensure user following the company, and set the followed status in result.
        UserFollowing userFollowing = new UserFollowing();
        userFollowing.setUserId(currentUser.getId());
        userFollowing.setOrganizationId(organizationObject.getId());
        userFollowDomain.ensureFollow(userFollowing, false);
        UserFollowing userFollowingResult = userFollowDomain.getUserFollowing(currentUser.getId(), organizationObject.getId());
        if (userFollowingResult != null) {
            scanResult.setFollowed_org(userFollowingResult.getIsFollowing());
        } else {
            scanResult.setFollowed_org(false);
        }

        //8, set validation result by our validation strategy.
        scanResult.setValidationResult(ValidateProduct.validateProduct(scanResult.getProduct(), currentUser, scanRecordList));

        //9, save scan Record
        long scanSave = SaveScanRecord(currentUser, currentExistProduct, scanRequestBody);
        return scanResult;
    }

    //能够访问所有的Key,为WebScan调用，因此每次Scan都!!不会save扫描记录。
    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public ScanResult getDetailWithoutSaveScanRecord(@PathVariable(value = "key") String key) {

        //1，validate input
        if (key == null || key.isEmpty()) {
            throw new BadRequestException("Key不能为空！");
        }

        ScanResult scanResult = new ScanResult();
        scanResult.setKey(key);

        //2, set product information
        Product currentExistProduct = productDomain.getProductByKey(key);
        if (currentExistProduct == null) {
            //Not found by the product Key
            LOGGER.warn("Key = {0} 不存在！", key);
            scanResult.setValidationResult(ValidationResult.Fake);  //no such key in our Yunsoo Platform.
            return scanResult;
        }
        scanResult.setProduct(currentExistProduct);

        //3, retrieve scan records
        List<ScanRecord> scanRecordList = dataAPIClient.get("scan/filterby?productKey={productKey}&pageSize={pageSize}",
                new ParameterizedTypeReference<List<ScanRecord>>() {
                }, key, 20);  //hard code as top 20 (desc order by created time )
        scanResult.setScanRecordList(scanRecordList);
        scanResult.setScanCounter(scanRecordList.size() + 1); //设置当前是第几次被最终用户扫描 - 根据用户扫描记录表.

        //4, retrieve logistics information
        scanResult.setLogisticsList(getLogisticsInfo(key));

        //5, get company information.
        OrganizationObject organizationObject = dataAPIClient.get("organization/{id}", OrganizationObject.class, scanResult.getProduct().getOrgId());
        scanResult.setManufacturer(new Organization(organizationObject));

        //6, set validation result by our validation strategy.
        scanResult.setValidationResult(scanRecordList.size() == 0 ? ValidationResult.Real : ValidationResult.Uncertain);

        return scanResult;
    }

    @RequestMapping(value = "/history/user/{userId}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
//    @PreAuthorize("hasPermission(#scanrecord, 'scanrecord:read')")
    public List<ScanRecord> getUserScanRecordsByFilter(
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
        this.fillProductInfor(scanRecordList);
        return scanRecordList;
    }

    @RequestMapping(value = "/searchback/{isbackward}/user/{userId}/from/{Id}/paging/{pageIndex}/{pageSize}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#scanrecord, 'scanrecord:read')")
    public List<ScanRecord> getUserScanRecordsByFilter(
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
        this.fillProductInfor(scanRecordList);
        return scanRecordList;
    }

    @RequestMapping(value = "/record/{key}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
//    @PreAuthorize("hasPermission(#scanrecord, 'scanrecord:read')")
    public List<ScanRecord> getScanRecordsByFilter(
            @PathVariable(value = "key") String key,
            @PathVariable(value = "pageIndex") Integer pageIndex,
            @PathVariable(value = "pageSize") Integer pageSize) {

        //验证输入参数
        if (key.isEmpty()) {
            throw new BadRequestException(40001, "Key不应为空！");
        }
        if (pageIndex < 0) {
            throw new BadRequestException("pageIndex不应小于0！");
        }
        if (pageSize <= 0) {
            throw new BadRequestException("PageSize不应小于等于0！");
        }

        List<ScanRecord> scanRecordList = dataAPIClient.get("scan/filterby?productKey={key}&pageIndex={pageIndex}&pageSize={pageSize}",
                new ParameterizedTypeReference<List<ScanRecord>>() {
                }, key, pageIndex, pageSize);
        //List<ScanRecord> scanRecordList = Arrays.asList(scanRecords == null ? new ScanRecord[0] : scanRecords);
        return scanRecordList;
    }

    private List<Logistics> getLogisticsInfo(String key) {
        List<LogisticsPath> logisticsPaths;
        try {
            logisticsPaths = logisticsDomain.getLogisticsPathsOrderByStartDate(key);
        } catch (NotFoundException ex) {
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
        ScanRecordObject scanRecord = new ScanRecordObject();
        scanRecord.setUserId(currentUser.getId());
        scanRecord.setDeviceId(scanRequestBody.getDeviceCode());
        if (scanRequestBody.getAppId() != null && !scanRequestBody.getAppId().isEmpty()) {
            scanRecord.setAppId(scanRequestBody.getAppId()); //记录扫描客户端
        } else {
            scanRecord.setAppId("未知");
        }
        scanRecord.setProductKey(currentProduct.getProductKey());
        scanRecord.setBaseProductId(currentProduct.getProductBaseId());
        if (scanRequestBody.getDetail() != null && !scanRequestBody.getDetail().isEmpty()) {
            scanRecord.setDetail(scanRequestBody.getDetail()); //接受扫描的相关详情
        } else {
            scanRecord.setDetail("匿名用户扫描");
        }
        scanRecord.setLongitude(scanRequestBody.getLongitude());
        scanRecord.setLatitude(scanRequestBody.getLatitude());
        if (scanRequestBody.getLocation() != null && !scanRequestBody.getLocation().isEmpty()) {
            scanRecord.setLocation(scanRequestBody.getLocation());
        } else {
            scanRecord.setLocation("Address:未公开;"); //用户选择不公开隐私地址信息
        }
        return dataAPIClient.post("scan", scanRecord, long.class);
    }

    private void fillProductInfor(List<ScanRecord> scanRecordList) {
        //fill product name
        HashMap<String, ProductBaseObject> productHashMap = new HashMap<>();
        for (ScanRecord scanRecord : scanRecordList) {
            if (!productHashMap.containsKey(scanRecord.getBaseProductId())) {
                ProductBaseObject productBaseObject = dataAPIClient.get("productbase/{id}", ProductBaseObject.class, scanRecord.getBaseProductId());
                if (productBaseObject != null) {
                    productHashMap.put(scanRecord.getBaseProductId(), productBaseObject);
                    scanRecord.setProductName(productBaseObject.getName());
                    scanRecord.setProductComment(productBaseObject.getComments());
                }
            } else {
                scanRecord.setProductName(productHashMap.get(scanRecord.getBaseProductId()).getName());
                scanRecord.setProductComment(productHashMap.get(scanRecord.getBaseProductId()).getComments());
            }
        }
    }
}

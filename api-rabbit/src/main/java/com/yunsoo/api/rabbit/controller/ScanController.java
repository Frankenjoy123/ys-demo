package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.Constants;
import com.yunsoo.api.rabbit.biz.ValidateProduct;
import com.yunsoo.api.rabbit.domain.*;
import com.yunsoo.api.rabbit.dto.*;
import com.yunsoo.api.rabbit.object.ValidationResult;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.api.rabbit.security.UserAuthentication;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.UserScanRecordObject;
import com.yunsoo.common.util.DateTimeUtils;
import com.yunsoo.common.util.KeyGenerator;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ScanDomain scanDomain;

    @Autowired
    private UserFollowDomain userFollowDomain;

    @Autowired
    private ProductDomain productDomain;

    @Autowired
    private ProductBaseDomain productBaseDomain;

    @Autowired
    private UserLikedProductDomain userLikedProductDomain;

    @Autowired
    private OrganizationDomain organizationDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScanController.class);

    //能够访问所有的Key,为移动客户端调用，因此每次Scan都save扫描记录。
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ScanResult scan(
            @RequestHeader(value = Constants.HttpHeaderName.APP_ID, required = false) String appId,
            @RequestHeader(value = Constants.HttpHeaderName.DEVICE_ID, required = false) String deviceId,
            @RequestHeader(value = Constants.HttpHeaderName.ACCESS_TOKEN, required = false) String accessToken,
            @RequestBody ScanRequest scanRequest) {
        String productKey = scanRequest.getKey();

        //validate input
        if (!KeyGenerator.validate(productKey)) {
            throw new BadRequestException("key not valid");
        }
        //1. get userId
        UserAuthentication userAuthentication = tokenAuthenticationService.getAuthentication(accessToken);
        String userId = userAuthentication != null ? userAuthentication.getDetails().getId() : Constants.Ids.ANONYMOUS_USER_ID;

        ScanResult scanResult = new ScanResult();
        scanResult.setUserId(userId);
        scanResult.setKey(productKey);

        //2. get product information
        Product product = productDomain.getProductByKey(productKey);
        if (product == null) {
            LOGGER.warn("product not found by [key: {}]", productKey);
            scanResult.setValidationResult(ValidationResult.Fake);
            return scanResult;
        }
        scanResult.setProduct(product);

        //3. set if user liked this product
        UserLikedProduct userLikedProduct = this.userLikedProductDomain.getUserLikedProduct(userId, product.getProductBaseId());
        if (userLikedProduct != null) {
            scanResult.setLiked_product(userLikedProduct.getActive());
        } else {
            scanResult.setLiked_product(false);
        }

        //4. retrieve scan records
        List<ScanRecord> scanRecordList = scanDomain.getScanRecordsByProductKey(scanRequest.getKey(), new PageRequest(0, 20))
                .map(ScanRecord::new)
                .getContent();
        scanResult.setScanRecordList(scanRecordList);
        scanResult.setScanCounter(scanRecordList.size() + 1); //设置当前是第几次被最终用户扫描 - 根据用户扫描记录表.

        //5. retrieve logistics information
        scanResult.setLogisticsList(getLogisticsInfo(scanRequest.getKey()));

        //6. get company information.
        OrganizationObject organizationObject = organizationDomain.getById(product.getOrgId());
        scanResult.setManufacturer(new Organization(organizationObject));

        if (scanRequest.getAutoFollowing() != null && scanRequest.getAutoFollowing()) {
            //7.1 ensure user following the company, and set the followed status in result.
            if (userFollowDomain.ensureUserOrganizationFollowing(userId, organizationObject.getId()) != null) {
                scanResult.setFollowed_org(true);
            } else {
                scanResult.setFollowed_org(false);
            }

            //7.2. ensure user following the product
            userFollowDomain.ensureUserProductFollowing(userId, product.getProductBaseId());
        } else {
            scanResult.setFollowed_org(false);
        }

        //8. set validation result by our validation strategy.
        scanResult.setValidationResult(ValidateProduct.validateProduct(scanResult.getProduct(), userId, scanRecordList));

        //9. save scan Record
        UserScanRecordObject userScanRecordObject = scanRequest.toUserScanRecordObject();
        userScanRecordObject.setProductKey(productKey);
        userScanRecordObject.setUserId(userId);
        userScanRecordObject.setProductBaseId(product.getProductBaseId());
        userScanRecordObject.setAppId(appId);
        userScanRecordObject.setDeviceId(deviceId);
        if (userScanRecordObject.getDetails() == null) {
            userScanRecordObject.setDetails("匿名用户扫描");
        }
        scanDomain.createScanRecord(userScanRecordObject);

        return scanResult;
    }

    //能够访问所有的Key,为WebScan调用，因此每次Scan都!!不会save扫描记录。
    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public ScanResult getScanResult(@PathVariable(value = "key") String key) {
        //1. validate input
        if (!KeyGenerator.validate(key)) {
            throw new BadRequestException("key not valid");
        }

        ScanResult scanResult = new ScanResult();
        scanResult.setKey(key);

        //2. get product information
        Product product = productDomain.getProductByKey(key);
        if (product == null) {
            LOGGER.warn("product not found by [key: {}]", key);
            scanResult.setValidationResult(ValidationResult.Fake);
            return scanResult;
        }
        scanResult.setProduct(product);

        //3. retrieve scan records
        List<ScanRecord> scanRecordList = scanDomain.getScanRecordsByProductKey(key, new PageRequest(0, 20))
                .map(ScanRecord::new)
                .getContent();
        scanResult.setScanRecordList(scanRecordList);
        scanResult.setScanCounter(scanRecordList.size() + 1); //设置当前是第几次被最终用户扫描 - 根据用户扫描记录表.

        //4. retrieve logistics information
        scanResult.setLogisticsList(getLogisticsInfo(key));

        //5. get company information.
        OrganizationObject organizationObject = organizationDomain.getById(product.getOrgId());
        scanResult.setManufacturer(new Organization(organizationObject));

        //6. set validation result by our validation strategy.
        scanResult.setValidationResult(scanRecordList.size() == 0 ? ValidationResult.Real : ValidationResult.Uncertain);

        return scanResult;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort is: createdDateTime,desc. " +
                            "Multiple sort criteria are supported.")
    })
    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public List<ScanRecord> getScanRecordsByFilter(
            @RequestParam(value = "product_key", required = false) String productKey,
            @ApiIgnore
            @PageableDefault(page = 0, size = 10, sort = "createdDateTime", direction = Sort.Direction.DESC)
            Pageable pageable,
            HttpServletResponse response) {
        Page<UserScanRecordObject> page;
        if (!StringUtils.isEmpty(productKey)) {
            //get the scan records by product key include other user's
            page = scanDomain.getScanRecordsByProductKey(productKey, pageable);
        } else {
            //get the scan records for current user only
            String userId = tokenAuthenticationService.getAuthentication().getDetails().getId();
            page = scanDomain.getScanRecordsByUserId(userId, pageable);
        }

        if (pageable != null) {
            response.setHeader("Content-Range", page.toContentRange());
        }
        List<ScanRecord> scanRecords = page.map(ScanRecord::new).getContent();
        fillProductInfo(scanRecords);
        return scanRecords;
    }


    private List<Logistics> getLogisticsInfo(String key) {
        List<LogisticsPath> logisticsPaths;
        try {
            logisticsPaths = logisticsDomain.getLogisticsPathsOrderByStartDate(key);
        } catch (NotFoundException ex) {
            return null;
        }

        List<Logistics> logisticsList = new ArrayList<>();
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

    private void fillProductInfo(List<ScanRecord> scanRecords) {
        Map<String, ProductBaseObject> map = new HashMap<>();
        for (ScanRecord scanRecord : scanRecords) {
            ProductBaseObject productBaseObject = map.get(scanRecord.getProductBaseId());
            if (productBaseObject == null) {
                productBaseObject = productBaseDomain.getProductBaseById(scanRecord.getProductBaseId());
                if (productBaseObject != null) {
                    map.put(productBaseObject.getId(), productBaseObject);
                }
            }
            if (productBaseObject != null) {
                scanRecord.setProductName(productBaseObject.getName());
                scanRecord.setProductDescription(productBaseObject.getDescription());
            }
        }
    }

}

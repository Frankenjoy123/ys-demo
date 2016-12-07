package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.Constants;
import com.yunsoo.api.rabbit.auth.dto.Organization;
import com.yunsoo.api.rabbit.auth.service.AuthOrganizationService;
import com.yunsoo.api.rabbit.domain.ProductBaseDomain;
import com.yunsoo.api.rabbit.domain.UserFollowDomain;
import com.yunsoo.api.rabbit.domain.UserScanDomain;
import com.yunsoo.api.rabbit.dto.ScanRequest;
import com.yunsoo.api.rabbit.dto.ScanResponse;
import com.yunsoo.api.rabbit.key.dto.Product;
import com.yunsoo.api.rabbit.key.service.ProductService;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.api.rabbit.security.UserAuthentication;
import com.yunsoo.api.rabbit.util.IpUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.UserOrganizationFollowingObject;
import com.yunsoo.common.data.object.UserProductFollowingObject;
import com.yunsoo.common.data.object.UserScanRecordObject;
import com.yunsoo.common.util.KeyGenerator;
import com.yunsoo.common.util.ObjectIdGenerator;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/2/27
 * Descriptions: Allow anonymous visit.
 */

@RestController
@RequestMapping("/scan")
public class ScanController {

    @Autowired
    private UserScanDomain userScanDomain;

    @Autowired
    private UserFollowDomain userFollowDomain;

    @Autowired
    private ProductBaseDomain productBaseDomain;

    @Autowired
    private ProductService productService;

    @Autowired
    private AuthOrganizationService authOrganizationService;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    private Log log = LogFactory.getLog(this.getClass());

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ScanResponse postScan(@RequestHeader(value = Constants.HttpHeaderName.APP_ID, required = true) String appId,
                                 @RequestHeader(value = Constants.HttpHeaderName.DEVICE_ID, required = false) String deviceId,
                                 @RequestHeader(value = Constants.HttpHeaderName.ACCESS_TOKEN, required = false) String accessToken,
                                 @RequestBody ScanRequest scanRequest,
                                 HttpServletRequest httpServletRequest) {
        String key = scanRequest.getKey();
        String ip = IpUtils.getIpFromRequest(httpServletRequest);
        UserAuthentication userAuthentication = tokenAuthenticationService.getAuthentication(accessToken);
        String userId = userAuthentication != null ? userAuthentication.getDetails().getId() : Constants.Ids.ANONYMOUS_USER_ID;

        //search product by key
        Product product = getProductByKey(key);

        //save scan record
        UserScanRecordObject userScanRecordObject = saveScanRecord(
                key,
                userId,
                product.getProductBaseId(),
                product.getKeyBatchId(),
                appId,
                deviceId,
                ip,
                scanRequest);

        ScanResponse scanResponse = new ScanResponse();

        //get product info
        ProductBaseObject productBaseObject = getProductBaseById(product.getProductBaseId());
        ScanResponse.Product productResponse = new ScanResponse.Product();
        productResponse.setId(productBaseObject.getId());
        productResponse.setName(productBaseObject.getName());
        productResponse.setDescription(productBaseObject.getDescription());
        if (StringUtils.hasText(productBaseObject.getImage())) {
            productResponse.setImageUrl(productBaseDomain.getProductBaseImageUrl(productBaseObject.getImage()));
        }
        productResponse.setShelfLife(productBaseObject.getShelfLife());
        productResponse.setShelfLifeInterval(productBaseObject.getShelfLifeInterval());
        //specific product info
        productResponse.setKey(key);
        productResponse.setStatusCode(product.getStatusCode());
        productResponse.setManufacturingDatetime(product.getManufacturingDateTime());
        scanResponse.setProduct(productResponse);

        //get organization info
        scanResponse.setOrganization(toOrganization(getOrganizationById(productBaseObject.getOrgId())));

        //set current scan record
        scanResponse.setScanRecord(toScanRecord(userScanRecordObject));

        //get security info
        scanResponse.setSecurity(getSecurityInfo(product));

        //social info
        ScanResponse.Social social = new ScanResponse.Social();
        if (scanRequest.getAutoFollowing() != null && scanRequest.getAutoFollowing()) {
            // ensure user following the company, and set the followed status in result.
            userFollowDomain.ensureUserOrganizationFollowing(userId, productBaseObject.getOrgId());
            // ensure user following the product
            userFollowDomain.ensureUserProductFollowing(userId, product.getProductBaseId());
            social.setOrgFollowing(true);
            social.setProductFollowing(true);
        } else {
            UserOrganizationFollowingObject userOrganizationFollowingObject = userFollowDomain.getUserOrganizationFollowingByUserIdAndOrgId(userId, productBaseObject.getOrgId());
            UserProductFollowingObject userProductFollowingObject = userFollowDomain.getUserProductFollowingByUserIdAndProductBaseId(userId, product.getProductBaseId());
            social.setOrgFollowing(userOrganizationFollowingObject != null);
            social.setProductFollowing(userProductFollowingObject != null);
        }
        scanResponse.setSocial(social);

        return scanResponse;
    }


    private Product getProductByKey(String key) {
        if (!KeyGenerator.validate(key)) {
            throw new NotFoundException("product not found");
        }
        Product product = productService.getProductByKey(key);
        if (product == null || product.getProductBaseId() == null) {
            throw new NotFoundException("product not found");
        }
        return product;
    }

    private ProductBaseObject getProductBaseById(String productBaseId) {
        if (!ObjectIdGenerator.validate(productBaseId)) {
            throw new NotFoundException("product not found");
        }
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(productBaseId);
        if (productBaseObject == null) {
            log.error("product not found by id: " + productBaseId);
            throw new NotFoundException("product not found");
        }
        return productBaseObject;
    }

    private Organization getOrganizationById(String orgId) {
        Organization org = authOrganizationService.getById(orgId);
        if (org == null) {
            log.error("organization not found by id: " + orgId);
            throw new NotFoundException("organization not found");
        }
        return org;
    }

    private UserScanRecordObject saveScanRecord(String productKey,
                                                String userId,
                                                String productBaseId,
                                                String productKeyBatchId,
                                                String appId,
                                                String deviceId,
                                                String ip,
                                                ScanRequest scanRequest) {
        UserScanRecordObject userScanRecordObject = new UserScanRecordObject();
        userScanRecordObject.setProductKey(productKey);
        userScanRecordObject.setUserId(userId);
        userScanRecordObject.setProductBaseId(productBaseId);
        userScanRecordObject.setProductKeyBatchId(productKeyBatchId);
        userScanRecordObject.setAppId(appId);
        userScanRecordObject.setDeviceId(deviceId);
        userScanRecordObject.setIp(ip);
        userScanRecordObject.setLongitude(scanRequest.getLongitude());
        userScanRecordObject.setLatitude(scanRequest.getLatitude());
        userScanRecordObject.setProvince(scanRequest.getProvince());
        userScanRecordObject.setCity(scanRequest.getCity());
        userScanRecordObject.setAddress(scanRequest.getAddress());
        userScanRecordObject.setDetails(scanRequest.getDetails());
        return userScanDomain.createScanRecord(userScanRecordObject);
    }

    private ScanResponse.Security getSecurityInfo(Product product) {
        ScanResponse.Security security = null;
        if (LookupCodes.ProductKeyType.QR_SECURE.equals(product.getKeyTypeCode())) {
            //防伪码
            security = new ScanResponse.Security();
            Page<UserScanRecordObject> userScanRecordObjectPage = userScanDomain.getScanRecordsByProductKey(product.getKey(), new PageRequest(0, 1));
            List<UserScanRecordObject> userScanRecordObjects = userScanRecordObjectPage.getContent();
            int scanCount = userScanRecordObjectPage.getCount() == null ? 0 : userScanRecordObjectPage.getCount();
            security.setScanCount(scanCount);
            if (userScanRecordObjects.size() > 0) {
                security.setFirstScan(toScanRecord(userScanRecordObjects.get(0)));
            }
            //set real if it's the first scan, otherwise set to uncertain.
            security.setVerificationResult(scanCount <= 1 ? Constants.VerificationResult.REAL : Constants.VerificationResult.UNCERTAIN);
        }
        return security;
    }

    private ScanResponse.Organization toOrganization(Organization org) {
        ScanResponse.Organization organization = new ScanResponse.Organization();
        organization.setId(org.getId());
        organization.setName(org.getName());
        organization.setStatusCode(org.getStatusCode());
        organization.setDescription(org.getDescription());
        return organization;
    }

    private ScanResponse.ScanRecord toScanRecord(UserScanRecordObject userScanRecordObject) {
        ScanResponse.ScanRecord scanRecord = new ScanResponse.ScanRecord();
        scanRecord.setId(userScanRecordObject.getId());
        scanRecord.setUserId(userScanRecordObject.getUserId());
        scanRecord.setProductKey(userScanRecordObject.getProductKey());
        scanRecord.setProductBaseId(userScanRecordObject.getProductBaseId());
        scanRecord.setDeviceId(userScanRecordObject.getDeviceId());
        scanRecord.setIp(userScanRecordObject.getIp());
        scanRecord.setLongitude(userScanRecordObject.getLongitude());
        scanRecord.setLatitude(userScanRecordObject.getLatitude());
        scanRecord.setProvince(userScanRecordObject.getProvince());
        scanRecord.setCity(userScanRecordObject.getCity());
        scanRecord.setAddress(userScanRecordObject.getAddress());
        scanRecord.setDetails(userScanRecordObject.getDetails());
        scanRecord.setCreatedDateTime(userScanRecordObject.getCreatedDateTime());
        return scanRecord;
    }

}

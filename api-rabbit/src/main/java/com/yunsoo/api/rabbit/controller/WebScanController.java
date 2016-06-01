package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.Constants;
import com.yunsoo.api.rabbit.domain.*;
import com.yunsoo.api.rabbit.dto.ProductCategory;
import com.yunsoo.api.rabbit.dto.WebScanRequest;
import com.yunsoo.api.rabbit.dto.WebScanResponse;
import com.yunsoo.api.rabbit.util.IpUtils;
import com.yunsoo.api.rabbit.util.YSIDGenerator;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.util.KeyGenerator;
import com.yunsoo.common.util.ObjectIdGenerator;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-02-17
 * Descriptions:
 */
@RestController
@RequestMapping("webScan")
public class WebScanController {

    @Autowired
    private ProductDomain productDomain;

    @Autowired
    private ProductBaseDomain productBaseDomain;

    @Autowired
    private OrganizationDomain organizationDomain;

    @Autowired
    private UserScanDomain userScanDomain;

    @Autowired
    private MarketingDomain marketingDomain;

    @Autowired
    private UserBlockDomain userBlockDomain;

    //region 一物一码

    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public WebScanResponse getKeyScan(@PathVariable(value = "key") String key) {

        //search product by key
        ProductObject productObject = getProductByKey(key);
        WebScanResponse webScanResponse = getBasicInfo(productObject.getProductBaseId());

        //product info specific
        WebScanResponse.Product product = webScanResponse.getProduct();
        product.setBatchId(productObject.getProductKeyBatchId());
        product.setKey(key);
        product.setStatusCode(productObject.getProductStatusCode());
        product.setManufacturingDatetime(productObject.getManufacturingDateTime());
        product.setKeyDetails(productObject.getDetails());

        ProductKeyBatchObject productKeyBatchObject = productDomain.getProductKeyBatch(productObject.getProductKeyBatchId());
        if (productKeyBatchObject != null) {
            String productKeyBatchDetails = productDomain.getProductKeyBatchDetails(productKeyBatchObject.getOrgId(), productObject.getProductKeyBatchId());
            product.setBatchDetails(productKeyBatchDetails);
            //marketing info
            webScanResponse.setMarketing(getMarketingInfo(productKeyBatchObject.getMarketingId()));
        }

        //security info
        webScanResponse.setSecurity(getSecurityInfo(productObject));

        return webScanResponse;
    }

    @RequestMapping(value = "{key}", method = RequestMethod.POST)
    public WebScanResponse.ScanRecord postKeyScan(
            @PathVariable(value = "key") String key,
            @RequestHeader(value = "User-Agent", required = false) String userAgent,
            @RequestHeader(value = Constants.HttpHeaderName.APP_ID) String appId,
            @RequestHeader(value = Constants.HttpHeaderName.DEVICE_ID, required = false) String deviceId,
            @CookieValue(value = Constants.CookieName.YSID, required = false) String ysid,
            @RequestBody WebScanRequest webScanRequest,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        //validate request
        validateWebScanRequest(webScanRequest, httpServletRequest, ysid, userAgent);

        //search product by key
        ProductObject productObject = getProductByKey(key);

        //save scan record
        UserScanRecordObject userScanRecordObject = saveScanRecord(
                key,
                productObject.getProductBaseId(),
                productObject.getProductKeyBatchId(),
                appId,
                deviceId,
                webScanRequest);

        if (userAgent != null && ysid == null) {
            //set cookie YSID
            setCookie(httpServletResponse, webScanRequest.getYsid());
        }

        ProductBaseObject productBaseObject = getProductBaseById(productObject.getProductBaseId());
        String usId = userScanRecordObject.getYsid();

        if (!StringUtils.isEmpty(userScanRecordObject.getUserId()))
            usId = null;

        List<UserBlockObject> userBlockObjects = userBlockDomain.getUserBlockList(userScanRecordObject.getUserId(), usId, productBaseObject.getOrgId());
        if (!(userBlockObjects == null || userBlockObjects.size() == 0)) {
            throw new ForbiddenException("you are forbidden!");
        }

        return toScanRecord(userScanRecordObject);
    }


    //endregion

    //region 产品码

    @RequestMapping(value = "productbase/{id}", method = RequestMethod.GET)
    public WebScanResponse getProductBaseScan(@PathVariable(value = "id") String productBaseId) {

        //get basic info for product base by id
        WebScanResponse webScanResponse = getBasicInfo(productBaseId);

        //marketing info
        webScanResponse.setMarketing(null);

        return webScanResponse;
    }

    @RequestMapping(value = "productbase/{id}", method = RequestMethod.POST)
    public WebScanResponse.ScanRecord postProductBaseScan(
            @PathVariable(value = "id") String productBaseId,
            @RequestHeader(value = Constants.HttpHeaderName.APP_ID) String appId,
            @RequestHeader(value = Constants.HttpHeaderName.DEVICE_ID, required = false) String deviceId,
            @RequestHeader(value = "User-Agent", required = false) String userAgent,
            @CookieValue(value = Constants.CookieName.YSID, required = false) String ysid,
            @RequestBody WebScanRequest webScanRequest,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        //validate request
        validateWebScanRequest(webScanRequest, httpServletRequest, ysid, userAgent);

        //search product base by id
        ProductBaseObject productBaseObject = getProductBaseById(productBaseId);

        //save scan record
        UserScanRecordObject userScanRecordObject = saveScanRecord(
                null,
                productBaseObject.getId(),
                null,
                appId,
                deviceId,
                webScanRequest);

        if (userAgent != null && ysid == null) {
            //set cookie YSID
            setCookie(httpServletResponse, webScanRequest.getYsid());
        }

        return toScanRecord(userScanRecordObject);
    }

    //endregion

    private void validateWebScanRequest(WebScanRequest webScanRequest, HttpServletRequest httpServletRequest, String ysid, String userAgent) {
        if (!YSIDGenerator.validate(webScanRequest.getYsid())) {
            webScanRequest.setYsid(YSIDGenerator.validate(ysid) ? ysid : YSIDGenerator.getNew());
        }
        if (!IpUtils.validate(webScanRequest.getIp())) {
            webScanRequest.setIp(IpUtils.getIpFromRequest(httpServletRequest));
        }
        if (webScanRequest.getUserAgent() == null) {
            webScanRequest.setUserAgent(userAgent);
        }
    }

    private ProductObject getProductByKey(String key) {
        if (!KeyGenerator.validate(key)) {
            throw new NotFoundException("product not found");
        }
        ProductObject productObject = productDomain.getProduct(key);
        if (productObject == null || productObject.getProductBaseId() == null) {
            throw new NotFoundException("product not found");
        }
        return productObject;
    }

    private ProductBaseObject getProductBaseById(String productBaseId) {
        if (!ObjectIdGenerator.validate(productBaseId)) {
            throw new NotFoundException("product not found");
        }
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(productBaseId);
        if (productBaseObject == null) {
            throw new NotFoundException("product not found");
        }
        return productBaseObject;
    }

    private WebScanResponse getBasicInfo(String productBaseId) {
        WebScanResponse response = new WebScanResponse();

        ProductBaseObject productBaseObject = getProductBaseById(productBaseId);
        ProductCategory productCategory = productBaseDomain.getProductCategoryById(productBaseObject.getCategoryId());
        String productBaseDetails = productBaseDomain.getProductBaseDetails(productBaseObject.getOrgId(), productBaseObject.getId(), productBaseObject.getVersion());
        OrganizationObject organizationObject = organizationDomain.getById(productBaseObject.getOrgId());

        WebScanResponse.Product product = new WebScanResponse.Product();
        product.setId(productBaseObject.getId());
        product.setName(productBaseObject.getName());
        product.setCategory(productCategory);
        product.setDescription(productBaseObject.getDescription());
        product.setShelfLife(productBaseObject.getShelfLife());
        product.setShelfLifeInterval(productBaseObject.getShelfLifeInterval());
        product.setDetails(productBaseDetails);
        response.setProduct(product);

        if (organizationObject != null) {
            WebScanResponse.Organization organization = new WebScanResponse.Organization();
            organization.setId(organizationObject.getId());
            organization.setName(organizationObject.getName());
            organization.setStatusCode(organizationObject.getStatusCode());
            organization.setDescription(organizationObject.getDescription());
            organization.setDetails(organizationObject.getDetails());
            response.setOrganization(organization);
        }

        return response;
    }

    private WebScanResponse.Marketing getMarketingInfo(String marketingId) {
        WebScanResponse.Marketing marketing = null;
        if (marketingId != null) {
            MarketingObject marketingObject = marketingDomain.getMarketingById(marketingId);
            long now = DateTime.now().getMillis();
            if (marketingObject != null
                    && (marketingObject.getStartDateTime() == null || marketingObject.getStartDateTime().getMillis() <= now)
                    && (marketingObject.getEndDateTime() == null || marketingObject.getEndDateTime().getMillis() >= now)
                    && LookupCodes.MktStatus.PAID.equals(marketingObject.getStatusCode())) {
                marketing = new WebScanResponse.Marketing();
                marketing.setId(marketingId);
            }
        }
        return marketing;
    }

    private WebScanResponse.Security getSecurityInfo(ProductObject productObject) {
        WebScanResponse.Security security = null;
        if (LookupCodes.ProductKeyType.QR_SECURE.equals(productObject.getProductKeyTypeCode())) {
            //防伪码
            security = new WebScanResponse.Security();
            Page<UserScanRecordObject> userScanRecordObjectPage = userScanDomain.getScanRecordsByProductKey(productObject.getProductKey(), new PageRequest(0, 20));
            List<UserScanRecordObject> userScanRecordObjects = userScanRecordObjectPage.getContent();
            security.setScanCount(userScanRecordObjectPage.getCount() == null ? 0 : userScanRecordObjectPage.getCount());
            if (userScanRecordObjects.size() > 0) {
                security.setFirstScan(toScanRecord(userScanRecordObjects.get(0)));

                List<WebScanResponse.ScanRecord> scanRecords = new ArrayList<>();
                for (UserScanRecordObject scanRecord : userScanRecordObjects) {
                    scanRecords.add(toScanRecord(scanRecord));
                }

                security.setScanRecords(scanRecords);
            }
        }
        return security;
    }

    private UserScanRecordObject saveScanRecord(String productKey,
                                                String productBaseId,
                                                String productKeyBatchId,
                                                String appId,
                                                String deviceId,
                                                WebScanRequest webScanRequest) {
        UserScanRecordObject userScanRecordObject = new UserScanRecordObject();
        userScanRecordObject.setProductKey(productKey);
        userScanRecordObject.setProductBaseId(productBaseId);
        userScanRecordObject.setProductKeyBatchId(productKeyBatchId);
        userScanRecordObject.setAppId(appId);
        userScanRecordObject.setYsid(webScanRequest.getYsid());
        userScanRecordObject.setDeviceId(deviceId);
        userScanRecordObject.setIp(webScanRequest.getIp());
        userScanRecordObject.setLongitude(webScanRequest.getLongitude());
        userScanRecordObject.setLatitude(webScanRequest.getLatitude());
        userScanRecordObject.setProvince(webScanRequest.getProvince());
        userScanRecordObject.setCity(webScanRequest.getCity());
        userScanRecordObject.setAddress(webScanRequest.getAddress());
        userScanRecordObject.setDetails(webScanRequest.getDetails());
        userScanRecordObject.setUserAgent(webScanRequest.getUserAgent());
        userScanRecordObject.setUserId(webScanRequest.getUserId());
        return userScanDomain.createScanRecord(userScanRecordObject);
    }

    private void setCookie(HttpServletResponse httpServletResponse, String ysid) {
        Cookie cookie = new Cookie(Constants.CookieName.YSID, ysid);
        cookie.setMaxAge(630720000);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }

    private WebScanResponse.ScanRecord toScanRecord(UserScanRecordObject userScanRecordObject) {
        WebScanResponse.ScanRecord scanRecord = new WebScanResponse.ScanRecord();
        scanRecord.setId(userScanRecordObject.getId());
        scanRecord.setUserId(userScanRecordObject.getUserId());
        scanRecord.setProductKey(userScanRecordObject.getProductKey());
        scanRecord.setProductBaseId(userScanRecordObject.getProductBaseId());
        scanRecord.setYsid(userScanRecordObject.getYsid());
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

package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.Constants;
import com.yunsoo.api.rabbit.domain.OrganizationDomain;
import com.yunsoo.api.rabbit.domain.ProductBaseDomain;
import com.yunsoo.api.rabbit.domain.ProductDomain;
import com.yunsoo.api.rabbit.domain.UserScanDomain;
import com.yunsoo.api.rabbit.dto.ProductCategory;
import com.yunsoo.api.rabbit.dto.WebScanRequest;
import com.yunsoo.api.rabbit.dto.WebScanResponse;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.data.object.UserScanRecordObject;
import com.yunsoo.common.util.KeyGenerator;
import com.yunsoo.common.util.ObjectIdGenerator;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

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


    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public WebScanResponse getScan(@PathVariable(value = "key") String key) {
        ProductObject productObject = getProductByKey(key);
        WebScanResponse response = getBasicInfo(productObject.getProductBaseId());

        response.getProduct().setKey(key);
        if (productObject.getProductStatusCode() != null) {
            response.getProduct().setStatusCode(productObject.getProductStatusCode());
        }
        response.getProduct().setManufacturingDatetime(productObject.getManufacturingDateTime());

        if (LookupCodes.ProductKeyType.QR_SECURE.equals(productObject.getProductKeyTypeCode())) {
            //防伪码
            WebScanResponse.Security security = new WebScanResponse.Security();


            response.setSecurity(security);
        }


        return response;
    }

    @RequestMapping(value = "{key}", method = RequestMethod.POST)
    public WebScanResponse.ScanRecord postScan(
            @PathVariable(value = "key") String key,
            @RequestHeader(value = "User-Agent", required = false) String userAgent,
            @RequestHeader(value = Constants.HttpHeaderName.APP_ID) String appId,
            @RequestHeader(value = Constants.HttpHeaderName.DEVICE_ID, required = false) String deviceId,
            @CookieValue(value = Constants.CookieName.YSID, required = false) String ysid,
            @RequestBody WebScanRequest webScanRequest,
            HttpServletResponse httpServletResponse) {

        if (webScanRequest.getYsid() == null) {
            webScanRequest.setYsid(ysid != null ? ysid : UUID.randomUUID().toString().replace("-", ""));
        }
        if (webScanRequest.getUserAgent() == null) {
            webScanRequest.setUserAgent(userAgent);
        }

        ProductObject productObject = getProductByKey(key);

        UserScanRecordObject userScanRecordObject = new UserScanRecordObject();
        userScanRecordObject.setProductKey(key);
        userScanRecordObject.setProductBaseId(productObject.getProductBaseId());
        userScanRecordObject.setAppId(appId);
        userScanRecordObject.setYsid(webScanRequest.getYsid());
        userScanRecordObject.setDeviceId(deviceId);
        userScanRecordObject.setLongitude(webScanRequest.getLongitude());
        userScanRecordObject.setLatitude(webScanRequest.getLatitude());
        userScanRecordObject.setProvince(webScanRequest.getProvince());
        userScanRecordObject.setCity(webScanRequest.getCity());
        userScanRecordObject.setAddress(webScanRequest.getAddress());
        userScanRecordObject.setDetails(webScanRequest.getDetails());
        userScanRecordObject.setUserAgent(webScanRequest.getUserAgent());
        userScanRecordObject = userScanDomain.createScanRecord(userScanRecordObject);

        if (userAgent != null && ysid == null) {
            //set cookie YSID
            setCookie(httpServletResponse, webScanRequest.getYsid());
        }

        return toScanRecord(userScanRecordObject);
    }

    @RequestMapping(value = "productbase/{id}", method = RequestMethod.GET)
    public WebScanResponse getProductBaseScan(@PathVariable(value = "id") String productBaseId) {
        return getBasicInfo(productBaseId);
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

    private WebScanResponse getBasicInfo(String productBaseId) {
        if (!ObjectIdGenerator.validate(productBaseId)) {
            throw new NotFoundException("product not found");
        }

        WebScanResponse response = new WebScanResponse();

        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(productBaseId);
        if (productBaseObject == null) {
            throw new NotFoundException("product not found");
        }
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

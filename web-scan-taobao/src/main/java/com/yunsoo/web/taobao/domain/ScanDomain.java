package com.yunsoo.web.taobao.domain;

import com.yunsoo.web.taobao.client.ApiRabbitClient;
import com.yunsoo.web.taobao.dto.BaiduIpLookupResult;
import com.yunsoo.web.taobao.dto.ScanRequest;
import com.yunsoo.web.taobao.dto.ScanResult;
import com.yunsoo.web.taobao.util.IpUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by:   Lijian
 * Created on:   2016-01-21
 * Descriptions:
 */
@Component
public class ScanDomain {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private ApiRabbitClient apiRabbitClient;

    @Autowired
    private IpLookupDomain ipLookupDomain;


    public ScanResult getScanResult(String productKey) {
        ScanResult scanResult = apiRabbitClient.get("scan/{key}", ScanResult.class, productKey);

        return scanResult;
    }

    public ScanResult scan(String productKey, HttpServletRequest request) {
        String ip = IpUtils.getIpFromRequest(request);
        BaiduIpLookupResult ipLookupResult = ipLookupDomain.ipLookup(ip);

        ScanRequest scanRequest = new ScanRequest();
        scanRequest.setKey(productKey);
        if (ipLookupResult != null && ipLookupResult.getRetData() != null) {
            BaiduIpLookupResult.RetData result = ipLookupResult.getRetData();
            scanRequest.setProvince(result.getProvince());
            scanRequest.setCity(result.getCity());
            scanRequest.setAddress(result.getProvince() + result.getCity() + result.getDistrict());
            scanRequest.setDetails("淘宝满天星集成环境扫描");
        }

        ScanResult scanResult = apiRabbitClient.post("scan", scanRequest, ScanResult.class);

        return scanResult;
    }


}

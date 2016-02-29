package com.yunsoo.web.taobao.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.web.taobao.dto.BaiduIpLookupResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Created by:   Lijian
 * Created on:   2016-01-21
 * Descriptions:
 */
@Component
public class IpLookupDomain {

    private Log log = LogFactory.getLog(this.getClass());
   // private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();

    public BaiduIpLookupResult ipLookup(String ip) {
        try {

          //   BaiduIpLookupResult result =   restTemplate.getForObject("http://apis.baidu.com/apistore/iplookupservice/iplookup?ip={ip}", BaiduIpLookupResult.class, ip);
            BaiduIpLookupResult result = null;
            if (result != null && !"0".equals(result.getErrNum())) {
                log.warn(String.format("ip lookup from baidu failed, [ip: %s, errMsg: %s]", ip, result.getErrMsg()));
            }
            return result;
        } catch (Exception ex) {
            log.error(String.format("ip lookup from baidu failed, [ip: %s]", ip), ex);
            return null;
        }
    }
}

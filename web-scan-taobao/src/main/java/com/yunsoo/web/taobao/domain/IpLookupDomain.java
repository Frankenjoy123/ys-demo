package com.yunsoo.web.taobao.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.web.taobao.dto.BaiduIpLookupResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by:   Lijian
 * Created on:   2016-01-21
 * Descriptions:
 */
@Component
public class IpLookupDomain {

    private Log log = LogFactory.getLog(this.getClass());
    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();

    public BaiduIpLookupResult ipLookup(String ip) {
        try {
            BaiduIpLookupResult result = restTemplate.execute("http://apis.baidu.com/apistore/iplookupservice/iplookup?ip={ip}",
                    HttpMethod.GET,
                    request -> {
                        HttpHeaders httpHeaders = request.getHeaders();
                        httpHeaders.set("apikey", "e235c907a6089c067e7dfccbbd6b31be");
                    }, response -> {
                        if (response.getStatusCode() == HttpStatus.OK) {
//                            byte[] buffer = StreamUtils.copyToByteArray(response.getBody());
//                            String body = new String(buffer, "GBK");
                            return objectMapper.readValue(response.getBody(), BaiduIpLookupResult.class);
                        }
                        return null;
                    }, ip);
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

package com.yunsoo.api.domain;

import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/11
 * Descriptions:
 */
@Component
public class DeviceDomain {

    @Autowired
    private RestClient dataApiClient;

    public void uploadLog(String appId, String deviceId, String fileName, long contentLength, InputStream inputStream) {
        String s3FileName = "application/{appId}/device/{deviceId}/log/{name}";
        ResourceInputStream stream = new ResourceInputStream(inputStream, contentLength, "text/plain");
        dataApiClient.put("file/s3?path=" + s3FileName, stream, appId, deviceId, fileName);

    }
}

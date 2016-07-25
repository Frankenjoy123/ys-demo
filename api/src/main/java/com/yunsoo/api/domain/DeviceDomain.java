package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.AttachmentObject;
import com.yunsoo.common.data.object.DeviceObject;
import com.yunsoo.common.util.RandomUtils;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/11
 * Descriptions:
 */
@Component
public class DeviceDomain {

    @Autowired
    private RestClient dataApiClient;

    public DeviceObject getById(String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return null;
        }
        try {
            return dataApiClient.get("device/{id}", DeviceObject.class, deviceId);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public Page<DeviceObject> getByFilterPaged(String orgId, String loginAccountId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("login_account_id", loginAccountId)
                .append(pageable)
                .build();
        return dataApiClient.getPaged("device" + query, new ParameterizedTypeReference<List<DeviceObject>>() {
        });
    }

    public void patchUpdate(DeviceObject deviceObject) {
        try {
            dataApiClient.patch("device/{id}", deviceObject, deviceObject.getId());
        } catch (NotFoundException ex) {
            throw new NotFoundException("device not found by id: " + deviceObject.getId());
        }
    }

    public void put(DeviceObject deviceObject) {
        dataApiClient.put("device/{id}", deviceObject, deviceObject.getId());
    }

    public void delete(String id) {
        if (StringUtils.hasText(id)) {
            dataApiClient.delete("device/{id}", id);
        }
    }

    public void uploadLog(String appId, String deviceId, String fileName, long contentLength, InputStream inputStream) {
        String s3FileName = "application/{appId}/device/{deviceId}/log/{name}";
        ResourceInputStream stream = new ResourceInputStream(inputStream, contentLength, "text/plain");
        dataAPIClient.put("file/s3?path=" + s3FileName, stream, appId, deviceId, fileName);

    }
}

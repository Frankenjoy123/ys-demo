package com.yunsoo.api.rabbit.domain;

import com.yunsoo.common.data.object.ApplicationObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by:   Lijian
 * Created on:   2015/9/7
 * Descriptions:
 */
@Component
public class ApplicationDomain {

    @Autowired
    private RestClient dataApiClient;

    public ApplicationObject getApplicationById(String id) {
        try {
            return dataApiClient.get("application/{id}", ApplicationObject.class, id);
        } catch (NotFoundException ignored) {
            return null;
        }
    }


    public ApplicationObject getLatestApplicationByTypeCode(String typeCode, String systemVersion) {
        QueryStringBuilder builder = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("type_code", typeCode).append("system_version", systemVersion );
        try {
            return dataApiClient.get("application/latest" + builder.toString(), ApplicationObject.class, typeCode);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

}

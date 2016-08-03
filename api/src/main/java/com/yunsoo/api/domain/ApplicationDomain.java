package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.ApplicationObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        try {
            return dataApiClient.get("application/{id}", ApplicationObject.class, id);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

}

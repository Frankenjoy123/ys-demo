package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.OperationLogObject;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by yan on 7/11/2016.
 */
@Component
public class OperationLogDomain {
    @Autowired
    private RestClient dataAPIClient;

    public OperationLogObject createLog(OperationLogObject log) {
        return dataAPIClient.post("operation", log, OperationLogObject.class);
    }
}

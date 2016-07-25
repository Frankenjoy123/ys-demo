package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.OperationLogObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yan on 7/11/2016.
 */
@Component
public class OperationLogDomain {
    @Autowired
    private RestClient dataApiClient;

    public OperationLogObject createLog(OperationLogObject log) {
        return dataApiClient.post("operation", log, OperationLogObject.class);
    }

    public Page<OperationLogObject> query(List<String> accountIds, String operation, String appId, DateTime start, DateTime end, Pageable pageable){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("operation", operation).append("app_id", appId).append("account_ids", accountIds)
                .append("create_datetime_start", start)
                .append("create_datetime_end", end)
                .append(pageable)
                .build();
        return dataApiClient.getPaged("operation" + query, new ParameterizedTypeReference<List<OperationLogObject>>() {
        });
    }
}

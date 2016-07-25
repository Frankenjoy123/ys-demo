package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.EMRUserProductEventStasticsObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Admin on 6/16/2016.
 */
@Component
public class EMRUserProductEventStasticsDomain {

    @Autowired
    private RestClient dataApiClient;

    public List<EMRUserProductEventStasticsObject> getEMRUserProductEventStasticsObjects(String orgId, String userId, String ysId, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("user_id", userId)
                .append("ys_id", ysId)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .build();

        return dataApiClient.get("emr/user/event_stats" + query, new ParameterizedTypeReference<List<EMRUserProductEventStasticsObject>>() {
        });
    }
}

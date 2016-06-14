package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.EMREventObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Admin on 6/13/2016.
 */
@Component
public class EMREventDomain {

    @Autowired
    private RestClient dataAPIClient;

    public Page<EMREventObject> getEMREventList(String orgId, String userId, String ysId, org.joda.time.LocalDate eventDateTimeStart, org.joda.time.LocalDate eventDateTimeEnd, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("user_id", userId)
                .append("ys_id", ysId)
                .append("event_datetime_start", eventDateTimeStart)
                .append("event_datetime_end", eventDateTimeEnd)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("emr/event" + query, new ParameterizedTypeReference<List<EMREventObject>>() {
        });
    }
}

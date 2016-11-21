package com.yunsoo.api.di.service;

import com.yunsoo.api.client.DiApiClient;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiaowu on 2016/11/21.
 */
@Service
public class EMREventService {
    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    private DiApiClient diApiClient;


    public Page<EMREventObject> getEMREventList(String orgId, String userId, String ysId, org.joda.time.LocalDate eventDateTimeStart, org.joda.time.LocalDate eventDateTimeEnd, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("user_id", userId)
                .append("ys_id", ysId)
                .append("event_datetime_start", eventDateTimeStart)
                .append("event_datetime_end", eventDateTimeEnd)
                .append(pageable)
                .build();

        return diApiClient.getPaged("event" + query, new ParameterizedTypeReference<List<EMREventObject>>() {
        });
    }

    public EMREventObject getLatestEMREvent(String orgId, String userId, String ysId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("user_id", userId)
                .append("ys_id", ysId)
                .build();

        return diApiClient.get("event/latest_consumption" + query, EMREventObject.class);
    }

    public PeriodUserConsumptionStatsObject getPeriodUserConsumptionStatsObject(String orgId, String userId, String ysId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("user_id", userId)
                .append("ys_id", ysId)
                .build();

        return diApiClient.get("event/period_consumption_stats" + query, PeriodUserConsumptionStatsObject.class);
    }
}

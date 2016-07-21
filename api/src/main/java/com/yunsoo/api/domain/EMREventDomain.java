package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.EMREventObject;
import com.yunsoo.common.data.object.PeriodUserConsumptionStatsObject;
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
    private RestClient dataApiClient;

    public Page<EMREventObject> getEMREventList(String orgId, String userId, String ysId, org.joda.time.LocalDate eventDateTimeStart, org.joda.time.LocalDate eventDateTimeEnd, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("user_id", userId)
                .append("ys_id", ysId)
                .append("event_datetime_start", eventDateTimeStart)
                .append("event_datetime_end", eventDateTimeEnd)
                .append(pageable)
                .build();

        return dataApiClient.getPaged("emr/event" + query, new ParameterizedTypeReference<List<EMREventObject>>() {
        });
    }

    public Page<EMREventObject> getEMREventFilterByShare(String orgId, String productBaseId, String province, String city, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append(pageable)
                .build();

        return dataApiClient.getPaged("emr/event/share" + query, new ParameterizedTypeReference<List<EMREventObject>>() {
        });
    }

    public Page<EMREventObject> getEMREventFilterByStoreUrl(String orgId, String productBaseId, String province, String city, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append(pageable)
                .build();

        return dataApiClient.getPaged("emr/event/store_url" + query, new ParameterizedTypeReference<List<EMREventObject>>() {
        });
    }

    public Page<EMREventObject> getEMREventFilterByComment(String orgId, String productBaseId, String province, String city, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append(pageable)
                .build();

        return dataApiClient.getPaged("emr/event/comment" + query, new ParameterizedTypeReference<List<EMREventObject>>() {
        });
    }


    public EMREventObject getLatestEMREvent(String orgId, String userId, String ysId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("user_id", userId)
                .append("ys_id", ysId)
                .build();

        return dataApiClient.get("emr/event/latest_consumption" + query, EMREventObject.class);
    }

    public PeriodUserConsumptionStatsObject getPeriodUserConsumptionStatsObject(String orgId, String userId, String ysId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("user_id", userId)
                .append("ys_id", ysId)
                .build();

        return dataApiClient.get("emr/event/period_consumption_stats" + query, PeriodUserConsumptionStatsObject.class);
    }
}

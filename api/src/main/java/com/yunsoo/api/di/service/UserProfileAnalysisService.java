package com.yunsoo.api.di.service;

import com.yunsoo.api.client.DiApiClient;
import com.yunsoo.common.data.object.EMREventLocationReportObject;
import com.yunsoo.common.data.object.EMREventReportObject;
import com.yunsoo.common.data.object.UserProfileLocationCountObject;
import com.yunsoo.common.data.object.UserProfileTagCountObject;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiaowu on 2016/11/21.
 */
@Service
public class UserProfileAnalysisService {
    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    private DiApiClient diApiClient;

    // region 消费者属性分析
    public List<UserProfileTagCountObject> getUserProfileAreaReport(String orgId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .build();
        return diApiClient.get("user_profile/area" + query, new ParameterizedTypeReference<List<UserProfileTagCountObject>>() {
        });
    }

    public List<UserProfileTagCountObject> getUserProfileGenderReport(String orgId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .build();
        return diApiClient.get("user_profile/gender" + query, new ParameterizedTypeReference<List<UserProfileTagCountObject>>() {
        });
    }



    public List<UserProfileTagCountObject> getUserProfileScanUsageReport(String orgId, LocalDate startTime, LocalDate endTime) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime)
                .build();
        return diApiClient.get("user_profile/scan_time_range" + query, new ParameterizedTypeReference<List<UserProfileTagCountObject>>() {
        });
    }

    public List<UserProfileTagCountObject> getUserProfileDeviceReport(String orgId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .build();
        return diApiClient.get("user_profile/device" + query, new ParameterizedTypeReference<List<UserProfileTagCountObject>>() {
        });
    }

    public List<UserProfileLocationCountObject> getUserProfileLocationReport(String orgId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).build();
        return diApiClient.get("user_profile/location" + query, new ParameterizedTypeReference<List<UserProfileLocationCountObject>>() {
        });
    }
    // endregion 消费者属性分析
}

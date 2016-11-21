package com.yunsoo.api.di.service;

import com.yunsoo.api.client.DiApiClient;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiaowu on 2016/11/21.
 */
@Service
public class EMRUserService {
    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    private DiApiClient diApiClient;

    public EMRUserObject getEMRUser(String orgId, String userId, String ysId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("user_id", userId)
                .append("ys_id", ysId)
                .build();

        return diApiClient.get("user/id" + query, EMRUserObject.class);
    }

    public Page<EMRUserObject> getEMRUserList(String orgId, Boolean sex, String phone, String name, String province, String city, Integer ageStart, Integer ageEnd, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd, String userTags, Boolean wxUser, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("sex", sex)
                .append("wx_user", wxUser)
                .append("phone", phone)
                .append("name", name)
                .append("province", province)
                .append("city", city)
                .append("user_tags", userTags)
                .append("age_start", ageStart)
                .append("age_end", ageEnd)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append(pageable)
                .build();

        return diApiClient.getPaged("user" + query, new ParameterizedTypeReference<List<EMRUserObject>>() {
        });
    }

    public EMRUserReportObject getEMRUserFunnelCount(String orgId, String productBaseId, String province, String city, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .build();

        return diApiClient.get("user/funnel" + query, new ParameterizedTypeReference<EMRUserReportObject>() {
        });
    }

}

package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.EMRUserObject;
import com.yunsoo.common.data.object.EMRUserReportObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EMRUserDomain {

    @Autowired
    private RestClient dataAPIClient;

    public EMRUserObject getEMRUser(String orgId, String userId, String ysId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("user_id", userId)
                .append("ys_id", ysId)
                .build();

        return dataAPIClient.get("emr/user/id" + query, EMRUserObject.class);
    }

    public Page<EMRUserObject> getEMRUserList(String orgId, Boolean sex, String phone, String name, String province, String city, Integer ageStart, Integer ageEnd, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("sex", sex)
                .append("phone", phone)
                .append("name", name)
                .append("province", province)
                .append("city", city)
                .append("age_start", ageStart)
                .append("age_end", ageEnd)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("emr/user" + query, new ParameterizedTypeReference<List<EMRUserObject>>() {
        });
    }

    public Page<EMRUserObject> getEMRScanUserList(String orgId, String productBaseId, String province, String city, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("emr/user/scan" + query, new ParameterizedTypeReference<List<EMRUserObject>>() {
        });
    }

    public Page<EMRUserObject> getEMRDrawUserList(String orgId, String productBaseId, String province, String city, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("emr/user/draw" + query, new ParameterizedTypeReference<List<EMRUserObject>>() {
        });
    }

    public Page<EMRUserObject> getEMRWXUserList(String orgId, String productBaseId, String province, String city, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("emr/user/wx" + query, new ParameterizedTypeReference<List<EMRUserObject>>() {
        });
    }

    public Page<EMRUserObject> getEMRWinUserList(String orgId, String productBaseId, String province, String city, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("emr/user/win" + query, new ParameterizedTypeReference<List<EMRUserObject>>() {
        });
    }

    public Page<EMRUserObject> getEMRRewardUserList(String orgId, String productBaseId, String province, String city, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("emr/user/reward" + query, new ParameterizedTypeReference<List<EMRUserObject>>() {
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

        return dataAPIClient.get("analysis/user/funnel" + query, new ParameterizedTypeReference<EMRUserReportObject>() {
        });
    }
}

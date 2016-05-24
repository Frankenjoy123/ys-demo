package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.EMRUserObject;
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

        return dataAPIClient.getPaged("emr/user" + query, new ParameterizedTypeReference<List<EMRUserObject>>() {});
    }

}

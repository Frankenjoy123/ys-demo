package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDomain {

    @Autowired
    private RestClient dataApiClient;

    public Page<UserObject> getUserList(Boolean sex, String phone, String name, String province, String city, Integer ageStart, Integer ageEnd, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
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

        return dataApiClient.getPaged("user/query" + query, new ParameterizedTypeReference<List<UserObject>>() {
        });
    }

    public UserObject getUserById(String id) {
        try {
            return dataApiClient.get("user/{id}", UserObject.class, id);
        } catch (NotFoundException ignored) {
            return null;
        }
    }


}

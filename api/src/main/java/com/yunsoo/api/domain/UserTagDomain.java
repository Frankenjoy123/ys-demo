package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.UserTagObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Admin on 6/14/2016.
 */
@Component
public class UserTagDomain {

    @Autowired
    private RestClient dataApiClient;

    public List<UserTagObject> getUserTags(String userId, String ysId, String orgId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("user_id", userId)
                .append("ys_id", ysId)
                .append("org_id", orgId)
                .build();

        return dataApiClient.get("/user/tag" + query, new ParameterizedTypeReference<List<UserTagObject>>() {
        });
    }

    public void updateTags(String userId, String ysId, String orgId, List<UserTagObject> userTagObjects) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("user_id", userId)
                .append("ys_id", ysId)
                .append("org_id", orgId)
                .build();

        dataApiClient.put("/user/tag" + query, userTagObjects);
    }
}

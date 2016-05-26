package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.UserBlockObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserBlockDomain {

    @Autowired
    private RestClient dataAPIClient;

    public List<UserBlockObject> getUserBlockList(String userId, String ysId, String orgId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("user_id", userId)
                .append("ys_id", ysId)
                .append("org_id", orgId)
                .build();

        return dataAPIClient.get("user/block" + query, new ParameterizedTypeReference<List<UserBlockObject>>() {
        });
    }

    public void delete(String userId, String ysId, String orgId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("user_id", userId)
                .append("ys_id", ysId)
                .append("org_id", orgId)
                .build();

        dataAPIClient.delete("user/block" + query);
    }

    public UserBlockObject create(UserBlockObject userBlockObject) {
        return dataAPIClient.post("user/block", userBlockObject, UserBlockObject.class);
    }

}

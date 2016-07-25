package com.yunsoo.api.domain;

import com.yunsoo.api.dto.User;
import com.yunsoo.common.data.object.UserOrganizationFollowingObject;
import com.yunsoo.common.data.object.UserProductFollowingObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by:   yan
 * Created on:   8/20/2015
 * Descriptions:
 */
@Component
public class UserFollowingDomain {

    @Autowired
    private RestClient dataApiClient;

    public Page<User> getFollowingUsersByOrgId(String orgId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append(pageable)
                .build();

        Page<UserOrganizationFollowingObject> userFollowingList = dataApiClient.getPaged("UserOrganizationFollowing" + query,
                new ParameterizedTypeReference<List<UserOrganizationFollowingObject>>() {
                });

        List<String> userIds = userFollowingList.getContent().stream().map(UserOrganizationFollowingObject::getUserId).collect(Collectors.toList());
        List<User> userList = getUsers(userIds);

        return new Page<>(userList, userFollowingList.getPage(), userFollowingList.getTotal());
    }

    public Page<User> getFollowingUsersByProductBaseId(String productBaseId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append(pageable).append("product_base_id", productBaseId)
                .build();

        Page<UserProductFollowingObject> userFollowingList = dataApiClient.getPaged("UserProductFollowing" + query,
                new ParameterizedTypeReference<List<UserProductFollowingObject>>() {
                });

        List<String> userIds = userFollowingList.getContent().stream().map(UserProductFollowingObject::getUserId).collect(Collectors.toList());
        List<User> userList = getUsers(userIds);

        return new Page<>(userList, userFollowingList.getPage(), userFollowingList.getTotal());
    }

    public Long getFollowingUsersCountByProductBaseId(String productBaseId) {
        return dataApiClient.get("UserProductFollowing/count?product_base_id={id}", Long.class, productBaseId);
    }

    public Map<String, Long> getProductFollowingTotalNumber(List<String> productBaseIds) {
        return dataApiClient.get("userproduct/following/count/{productBaseIds}", new ParameterizedTypeReference<Map<String, Long>>() {
        }, StringUtils.arrayToCommaDelimitedString(productBaseIds.toArray()));
    }


    private List<User> getUsers(List<String> userIds) {
        if (userIds == null || userIds.size() == 0) {
            return new ArrayList<>();
        } else {
            return dataApiClient.get("/user?id_in={ids}", new ParameterizedTypeReference<List<User>>() {
            }, StringUtils.collectionToCommaDelimitedString(userIds));
        }
    }
}

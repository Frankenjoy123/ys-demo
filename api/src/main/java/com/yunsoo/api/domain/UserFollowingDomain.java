package com.yunsoo.api.domain;

import com.yunsoo.api.dto.User;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yan on 8/20/2015.
 */
@Component
public class UserFollowingDomain {

    @Autowired
    private RestClient dataAPIClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserFollowingDomain.class);

    public Page<User> getUserOrganizationFollowingsByOrgId(String orgId, Pageable pageable){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append(pageable).append("orgid", orgId)
                .build();

        Page<UserOrganizationFollowingObject> userFollowingList = dataAPIClient.getPaged("/userorganization/following" + query, new ParameterizedTypeReference<List<UserOrganizationFollowingObject>>() {
        }, orgId);

        List<String> userIds = new ArrayList<String>();
        for(UserOrganizationFollowingObject item : userFollowingList){
            userIds.add(item.getUserId());
        }

        List<User> userList =  dataAPIClient.get("/user/multiple/{ids}", new ParameterizedTypeReference<List<User>>() {
        }, userIds.toArray());



        return new Page<User>(userList, userFollowingList.getPage(), userFollowingList.getTotal());
    }

    public Page<User> getUserProductFollowingsByProductId(String productId, Pageable pageable){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append(pageable).append("productid", productId)
                .build();

        Page<UserProductBaseFollowingObject> userFollowingList = dataAPIClient.getPaged("/userproduct/following" + query, new ParameterizedTypeReference<List<UserProductBaseFollowingObject>>() {
        }, productId);

        List<String> userIds = new ArrayList<String>();
        for(UserProductBaseFollowingObject item : userFollowingList){
            userIds.add(item.getUserId());
        }

        List<User> userList =  dataAPIClient.get("/user/multiple/{ids}", new ParameterizedTypeReference<List<User>>() {
        }, userIds.toArray());

        return new Page<User>(userList, userFollowingList.getPage(), userFollowingList.getTotal());
    }




}

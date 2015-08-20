package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.dto.basic.UserOrganizationFollowing;
import com.yunsoo.api.rabbit.dto.basic.UserProductFollowing;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.yunsoo.common.web.client.Page;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.util.QueryStringBuilder;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Zhe on 2015/4/24.
 */
@Component
public class UserFollowDomain {

    @Autowired
    private RestClient dataAPIClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserFollowDomain.class);

    public String ensureFollow(UserOrganizationFollowing userFollowing) {
        if (userFollowing == null) {
            throw new BadRequestException("UserOrganizationFollowing 不能为空！");
        }
        //check if exist in DB
        UserOrganizationFollowing existingUserFollowing = dataAPIClient.get("/userorganization/following/who/{id}/org/{orgid}", UserOrganizationFollowing.class, userFollowing.getUserId(), userFollowing.getOrgId());
        if (existingUserFollowing == null) {
            //user never follow org before, just add new record.
            userFollowing.setIsFollowing(true);
            String id = dataAPIClient.post("/userorganization/following", userFollowing, String.class);
            return id;
        }
        else
            return existingUserFollowing.getId();
    }

    public UserOrganizationFollowing getUserOrganizationFollowing(String userId, String orgId) {
        UserOrganizationFollowing userFollowing = dataAPIClient.get("/userorganization/following/who/{id}/org/{orgid}", UserOrganizationFollowing.class, userId, orgId);
        return userFollowing;
    }

    public Page<UserOrganizationFollowing> getUserOrganizationFollowingsByUserId(String userId, Pageable pageable){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append(pageable)
                .build();

        Page<UserOrganizationFollowing> userFollowingList = dataAPIClient.getPaged("/userorganization/following/who/{0}" + query, new ParameterizedTypeReference<List<UserOrganizationFollowing>>() {
        }, userId);

        //fill organization Name
        HashMap<String, OrganizationObject> orgMap = new HashMap<>();
        for (UserOrganizationFollowing userFollowing : userFollowingList) {
            if (!orgMap.containsKey(userFollowing.getOrgId())) {
                OrganizationObject object = dataAPIClient.get("organization/{id}", OrganizationObject.class, userFollowing.getOrgId());
                if (object != null) {
                    orgMap.put(userFollowing.getOrgId(), object);
                    userFollowing.setOrgName(object.getName());
                    userFollowing.setOrgDescription(object.getDescription());
                } else {
                    userFollowing.setOrgName(orgMap.get(userFollowing.getOrgId()).getName());
                    userFollowing.setOrgDescription(orgMap.get(userFollowing.getOrgId()).getDescription());
                }
            }
        }
        return userFollowingList;
    }

    public void deleteUserOrganizationFollowing(String id){
        dataAPIClient.delete("/userorganization/following/{id}",id);
    }


    public String ensureFollow(UserProductFollowing userFollowing) {
        if (userFollowing == null) {
            throw new BadRequestException("UserProductFollowing 不能为空！");
        }
        //check if exist in DB
        UserProductFollowing existingUserFollowing = getUserProductFollowing(userFollowing.getUserId(), userFollowing.getProductId());
        if (existingUserFollowing != null) {
            return existingUserFollowing.getId();
        } else {
            //user never follow org before, just add new record.
            userFollowing.setIsFollowing(true);
            String id = dataAPIClient.post("/userproduct/following", userFollowing, String.class);
            return id;
        }
    }

    public UserProductFollowing getUserProductFollowing(String userId, String productId) {

        UserProductFollowing userFollowing = dataAPIClient.get("/userproduct/following/who/{id}/product/{productid}", UserProductFollowing.class,userId, productId);
        return userFollowing;
    }

    public Page<UserProductFollowing> getUserProductFollowingsByUserId(String userId, Pageable pageable){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append(pageable)
                .build();

        Page<UserProductFollowing> userFollowingList = dataAPIClient.getPaged("/userproduct/following/who/{0}" + query, new ParameterizedTypeReference<List<UserProductFollowing>>() {
        }, userId);

        for (UserProductFollowing userFollowing : userFollowingList) {
            ProductBaseObject object = null;
            try {
                object = dataAPIClient.get("productbase/{id}", ProductBaseObject.class, userFollowing.getProductId());
            } catch (NotFoundException ex) {
                LOGGER.error("get product base id error: " + userFollowing.getProductId(), ex);
            }
            if (object != null) {
                userFollowing.setProductName(object.getName());
                userFollowing.setProductDescription(object.getDescription());
            }
            else{
                //if the product is not exist, should remove the item from following list
                userFollowing.setUserId(null);
            }
        }
        return userFollowingList;
    }


    public void deleteUserProductFollowing(String id){
        dataAPIClient.delete("/userproduct/following/{id}",id);
    }

}

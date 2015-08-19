package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.dto.basic.UserOrganizationFollowing;
import com.yunsoo.api.rabbit.dto.basic.UserProductFollowing;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Zhe on 2015/4/24.
 */
@Component
public class UserFollowDomain {

    @Autowired
    private RestClient dataAPIClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserFollowDomain.class);

//    public long ensureFollow(UserOrganizationFollowing userFollowing, Boolean forceFollow) {
//        return this.ensureFollow(userFollowing, forceFollow, false);
//    }

    public long ensureFollow(UserOrganizationFollowing userFollowing, Boolean forceFollow) {
        if (userFollowing == null) {
            throw new BadRequestException("UserOrganizationFollowing 不能为空！");
        }
        //check if exist in DB
        UserOrganizationFollowing existingUserFollowing = dataAPIClient.get("/userorganization/following/who/{id}/org/{orgid}", UserOrganizationFollowing.class, userFollowing.getUserId(), userFollowing.getOrganizationId());
        if (existingUserFollowing != null) {
            //when user is unfollowing org, and we need to force user to follow
            if (!existingUserFollowing.getIsFollowing() && forceFollow) {
                existingUserFollowing.setIsFollowing(true);
                dataAPIClient.patch("/userorganization/following", existingUserFollowing, long.class);
            }
            return existingUserFollowing.getId();
        } else {
            //user never follow org before, just add new record.
            userFollowing.setIsFollowing(true);
            Long id = dataAPIClient.post("/userorganization/following", userFollowing, long.class);
            return id;
        }
    }

    public long ensureFollow(UserProductFollowing userFollowing, Boolean forceFollow) {
        if (userFollowing == null) {
            throw new BadRequestException("UserProductFollowing 不能为空！");
        }
        //check if exist in DB
        UserProductFollowing existingUserFollowing = getUserProductFollowing(userFollowing.getUserId(), userFollowing.getProductId());
        if (existingUserFollowing != null) {
            //when user is unfollowing org, and we need to force user to follow
            if (!existingUserFollowing.getIsFollowing() && forceFollow) {
                existingUserFollowing.setIsFollowing(true);
                dataAPIClient.patch("/userproduct/following", existingUserFollowing, long.class);
            }
            return existingUserFollowing.getId();
        } else {
            //user never follow org before, just add new record.
            userFollowing.setIsFollowing(true);
            Long id = dataAPIClient.post("/userproduct/following", userFollowing, long.class);
            return id;
        }
    }


    public UserOrganizationFollowing getUserFollowing(String userId, String orgId) {
        UserOrganizationFollowing userFollowing = dataAPIClient.get("/userorganization/following/who/{id}/org/{orgid}", UserOrganizationFollowing.class, userId, orgId);
        return userFollowing;
    }


    public UserProductFollowing getUserProductFollowing(String userId, String productId) {

        UserProductFollowing userFollowing = dataAPIClient.get("/userproduct/following/who/{id}/product/{productid}", UserProductFollowing.class,userId, productId);


        return userFollowing;
    }
}

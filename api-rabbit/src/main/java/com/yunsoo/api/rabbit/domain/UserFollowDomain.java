package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.dto.basic.UserFollowing;
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

    public long ensureFollow(UserFollowing userFollowing) {
        if (userFollowing == null) {
            throw new BadRequestException("UserFollowing 不能为空！");
        }
        //check if exist in DB
        UserFollowing existingUserFollowing = dataAPIClient.get("/user/following/who/{id}/org/{orgid}", UserFollowing.class, userFollowing.getUserId(), userFollowing.getOrganizationId());
        if (existingUserFollowing != null) {
            if (existingUserFollowing.getIsFollowing()) {
                //when user is following org
                return existingUserFollowing.getId();
            } else {
                //when user is unfollowing org
                existingUserFollowing.setIsFollowing(true);
                dataAPIClient.patch("/user/following", existingUserFollowing, long.class);
                return existingUserFollowing.getId();

            }
        } else {
            //user never follow org before, just add new record.
            userFollowing.setIsFollowing(true);
            Long id = dataAPIClient.post("/user/following", userFollowing, long.class);
            return id;
        }
    }
}

package com.yunsoo.api.biz;

import com.yunsoo.api.dto.basic.User;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 个人用户的业务域。
 * This is the User business layer that consumes multi-dataAPI services.
 * Created by Zhe on 2015/3/17.
 */
@Component
public class UserDomain {

    @Autowired
    private RestClient dataAPIClient;

    //call dataAPI to get current User
    public User getUser(Integer userId, String deviceCode) {
        User user = null;
        if (userId != null && userId > 0) {
            user = dataAPIClient.get("user/id/{id}", User.class, userId);
        } else {
            user = dataAPIClient.get("user/token/{devicecode}", User.class, deviceCode);
        }
        return user;
    }
}

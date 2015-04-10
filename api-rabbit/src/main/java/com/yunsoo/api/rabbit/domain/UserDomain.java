package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.dto.basic.User;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDomain.class);

    //call dataAPI to get current User
    public User ensureUser(Integer userId, String deviceCode) {
        User user = null;
        if (userId != null && userId > 0) {
            try {
                user = dataAPIClient.get("user/id/{id}", User.class, userId);
            } catch (NotFoundException ex) {
                LOGGER.info("Notfound user id = {0}", userId);
            }
        } else {
            try {
                user = dataAPIClient.get("user/token/{devicecode}", User.class, deviceCode);
            } catch (NotFoundException ex) {
                LOGGER.info("Notfound user for deviceCode = {0}", deviceCode);
            }

            if (user == null) {
                User newUser = new User();
                newUser.setDeviceCode(deviceCode);
                newUser.setName(Long.toString(DateTime.now().getMillis())); //default name is the time.

                long id = dataAPIClient.post("user/create", newUser, Long.class); //save user
                if (id >= 0) {
                    newUser.setId(id);
                    return newUser;
                } else {
                    return null;
                }
            }
        }
        return user;
    }
}

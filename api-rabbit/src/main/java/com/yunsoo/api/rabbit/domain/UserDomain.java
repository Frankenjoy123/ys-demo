package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.dto.basic.User;
import com.yunsoo.api.rabbit.object.TAccountStatusEnum;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 个人用户的业务域。
 * This is the User business layer that consumes multi-dataAPI services.
 * Created by Zhe on 2015/3/17.
 */
@Component
public class UserDomain {

    @Autowired
    private RestClient dataAPIClient;
    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDomain.class);

    //call dataAPI to get current User
    public User ensureUser(String userId, String deviceCode, String cellular) {
        User user = null;
        if (userId != null) {
            try {
                user = dataAPIClient.get("user/id/{id}", User.class, userId);
            } catch (NotFoundException ex) {
                LOGGER.info("Notfound user id = {0}", userId);
            }
        } else {
            try {
                if (StringUtils.hasText(cellular)) {
                    user = dataAPIClient.get("user/cellular/{cellular}", User.class, cellular);
                }
            } catch (NotFoundException ex) {
                LOGGER.info("Notfound user for cellular = {0}", cellular);
            }

            try {
                if (StringUtils.hasText(deviceCode)) {
                    user = dataAPIClient.get("user/device/{devicecode}", User.class, deviceCode);
                }
            } catch (NotFoundException ex) {
                LOGGER.info("Notfound user for devicecode = {0}", cellular);
            }

            if (user == null) {
                User newUser = new User();
                newUser.setYsCreadit(0);  //set default properties.
                newUser.setLevel(1);
                newUser.setDeviceCode(deviceCode);
                newUser.setCellular(cellular);
                newUser.setName("求真名"); //default name is the time.
                newUser.setStatus(TAccountStatusEnum.ENABLED.value()); //default is enabled
                String id = dataAPIClient.post("user", newUser, String.class); //save user
                newUser.setId(id);
                return newUser;
            }
        }
        return user;
    }

    public Boolean validateToken(String token) {
        if (token == null || token.isEmpty()) return false;
        return tokenAuthenticationService.checkIdentity(token, null, false);
    }

    public Boolean validateToken(String token, String userId) {
        if (token == null || token.isEmpty()) return false;
        if (userId == null || userId.isEmpty()) return false;
        return tokenAuthenticationService.checkIdentity(token, userId, true);
    }

}

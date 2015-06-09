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
        if (!StringUtils.isEmpty(cellular)) {
            try {
                user = dataAPIClient.get("user/cellular/{cellular}", User.class, cellular);
            } catch (NotFoundException ex) {
                LOGGER.info("Notfound user for cellular = {0}", cellular);
            }

            //update device code if has new value. e.g. : rebind cellphone to new device
            if (user.getDeviceCode() != deviceCode) {
                user.setDeviceCode(deviceCode);
                dataAPIClient.patch("user", user);
            }
        } else {
            if (!StringUtils.isEmpty(userId)) {
                try {
                    user = dataAPIClient.get("user/id/{id}", User.class, userId);
                } catch (NotFoundException ex) {
                    LOGGER.info("Notfound user id = {0}", userId);
                }

                //update cellular and device code if has new value.
                if (user.getDeviceCode() != deviceCode) {
                    user.setDeviceCode(deviceCode);
                    dataAPIClient.patch("user", user);
                }
            }
        }

        if (user == null) {
            User newUser = this.generateDefaultUser();
            newUser.setDeviceCode(deviceCode);
            newUser.setCellular(cellular);
            if (!StringUtils.isEmpty(cellular)) {
                newUser.setStatus(TAccountStatusEnum.VERIFIED.value()); //if cellular is not null
            }
            return this.createNewUser(newUser);
        }
        return user;
    }

    //Always create new User
    public User generateDefaultUser() {
        User newUser = new User();
        newUser.setYsCreadit(100);  //set default properties.
        newUser.setLevel(1);
        newUser.setName("求真名"); //default name is the time.
        newUser.setStatus(TAccountStatusEnum.ENABLED.value()); //default is enabled
        return newUser;
    }

    //Always create new anonymous User
    public User createAnonymousUser(String deviceCode) {
        User newUser = this.generateDefaultUser();
        newUser.setDeviceCode(deviceCode);
        newUser.setStatus(TAccountStatusEnum.ENABLED.value());
        return createNewUser(newUser);
    }

    //Just create new User - call data-api
    public User createNewUser(User newUser) {
        String id = dataAPIClient.post("user", newUser, String.class); //save user
        newUser.setId(id);
        return newUser;
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

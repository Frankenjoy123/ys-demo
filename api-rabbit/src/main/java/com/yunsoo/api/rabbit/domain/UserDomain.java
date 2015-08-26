package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.Constants;
import com.yunsoo.api.rabbit.dto.User;
import com.yunsoo.api.rabbit.dto.basic.UserOrganizationFollowing;
import com.yunsoo.api.rabbit.object.TAccountStatusEnum;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.api.rabbit.security.UserAuthentication;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.util.ImageProcessor;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

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

    @Autowired
    private UserFollowDomain userFollowDomain;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDomain.class);

    public UserObject getUserById(String userId) {
        try {
            return dataAPIClient.get("user/{id}", UserObject.class, userId);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public List<UserObject> getUsersByPhone(String phone) {
        return dataAPIClient.get("user?phone={phone}", new ParameterizedTypeReference<List<UserObject>>() {
        }, phone);
    }

    public List<UserObject> getUsersByDeviceId(String deviceId) {
        return dataAPIClient.get("user?device_id={deviceId}", new ParameterizedTypeReference<List<UserObject>>() {
        }, deviceId);
    }

    public void patchUpdateUser(UserObject user) {
        dataAPIClient.patch("user/{id}", user, user.getId());
    }

    public ResourceInputStream getUserGravatar(String userId, String imageName) {
        try {
            return dataAPIClient.getResourceInputStream("file/s3?path=user/{userId}/gravatar/{imageName}", userId, imageName);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public void saveUserGravatar(String userId, String imageName, byte[] imageDataBytes) {
        try {
            ImageProcessor imageProcessor = new ImageProcessor().read(new ByteArrayInputStream(imageDataBytes));
            ByteArrayOutputStream image400x400OutputStream = new ByteArrayOutputStream();
            imageProcessor.resize(400, 400).write(image400x400OutputStream, "png");
            dataAPIClient.put("file/s3?path=user/{userId}/gravatar/{imageName}",
                    new ResourceInputStream(new ByteArrayInputStream(image400x400OutputStream.toByteArray()), image400x400OutputStream.size(), "image/png"),
                    userId, imageName);
        } catch (IOException e) {
            throw new InternalServerErrorException("gravatar upload failed [userId: " + userId + ", imageName: " + imageName + "]");
        }
    }


    //call dataAPI to get current User
    public User ensureUser(String userId, String deviceCode, String cellular) {
        User user = null;
        //Step 1: try ensure user by cellphone!
        if (!StringUtils.isEmpty(cellular)) {
            try {
                user = dataAPIClient.get("user/cellular/{cellular}", User.class, cellular);
                if (user != null) {
                    //update device code if has new value. e.g. : rebind cellphone to new device
                    if (deviceCode != null && !deviceCode.equals(user.getDeviceId())) {
                        user.setDeviceId(deviceCode);
                        dataAPIClient.patch("user", user);
                    }
                    return user; //find and return user by cellphone
                }
            } catch (NotFoundException ex) {
                LOGGER.info("Notfound user for cellular = {0}", cellular);
            }
        }

        //Step 2: try ensure user by existing user Id!
        if (!StringUtils.isEmpty(userId)) {
            try {
                user = dataAPIClient.get("user/{id}", User.class, userId);
                if (user != null) {
                    //update cellular and device code if has new value.
                    if (user.getPhone() != cellular || user.getDeviceId() != deviceCode) {
                        if (!StringUtils.isEmpty(cellular)) {
                            user.setPhone(cellular);
                        }
                        if (!StringUtils.isEmpty(deviceCode)) {
                            user.setDeviceId(deviceCode);
                        }
                        dataAPIClient.patch("user", user);
                    }
                    return user;  //return existing user.
                }

            } catch (NotFoundException ex) {
                LOGGER.info("Notfound user id = {0}", userId);
            }
        }

        //Step 3: ensure user if still can't get it!
        if (user == null) {
            User newUser = this.generateDefaultUser();
            newUser.setDeviceId(deviceCode);
            newUser.setPhone(cellular);
            if (!StringUtils.isEmpty(cellular)) {
                newUser.setStatusCode(LookupCodes.UserStatus.ENABLED); //if cellular is not null
            }
            return this.createNewUser(newUser);
        }
        return null;
    }

    //Always create new User
    public User generateDefaultUser() {
        User newUser = new User();
        newUser.setPoint(100);  //set default properties.
        newUser.setName("游客");
        newUser.setStatusCode(LookupCodes.UserStatus.ENABLED); //default is enabled
        return newUser;
    }

    //Always create new anonymous User
    public User createAnonymousUser(String deviceCode) {
        User newUser = this.generateDefaultUser();
        newUser.setDeviceId(deviceCode);
        newUser.setStatusCode(TAccountStatusEnum.ENABLED.value());
        return createNewUser(newUser);
    }

    //Just create new User - call data-api
    public User createNewUser(User newUser) {
        String id = dataAPIClient.post("user", newUser, String.class); //save user
        newUser.setId(id);

        //force following 云溯科技
        UserOrganizationFollowing userFollowing = new UserOrganizationFollowing();
        userFollowing.setUserId(id);
        userFollowing.setOrgId(Constants.Ids.YUNSU_ORG_ID); //get Yunsu's orgID
        userFollowDomain.ensureFollow(userFollowing);
        return newUser;
    }

    public Boolean validateUser(String userId) {
        UserAuthentication userAuthentication = tokenAuthenticationService.getAuthentication();
        return userAuthentication != null && userAuthentication.getDetails().getId().equals(userId);
    }

}

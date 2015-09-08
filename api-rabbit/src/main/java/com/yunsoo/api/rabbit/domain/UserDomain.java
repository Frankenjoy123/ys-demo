package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.Constants;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.api.rabbit.security.UserAuthentication;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.util.ImageProcessor;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.ConflictException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/3/17
 * Descriptions:
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
            return userId != null ? dataAPIClient.get("user/{id}", UserObject.class, userId) : null;
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public UserObject getUserByPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return null;
        }
        List<UserObject> userObjects = dataAPIClient.get("user?phone={phone}", new ParameterizedTypeReference<List<UserObject>>() {
        }, phone);
        if (userObjects.size() == 0) {
            return null;
        } else {
            return userObjects.get(userObjects.size() - 1);
        }
    }

    public List<UserObject> getUsersByDeviceId(String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return new ArrayList<>();
        }
        return dataAPIClient.get("user?device_id={deviceId}", new ParameterizedTypeReference<List<UserObject>>() {
        }, deviceId);
    }

    public void patchUpdateUser(UserObject userObject) {
        String userId = userObject.getId();
        if (!StringUtils.isEmpty(userId)) {
            //check phone
            if (userObject.getPhone() != null) {
                checkPhoneExists(userObject.getPhone(), userId);
            }
            dataAPIClient.patch("user/{id}", userObject, userId);
        }
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

    public UserObject createUser(UserObject userObject) {
        //check phone
        if (userObject.getPhone() != null) {
            checkPhoneExists(userObject.getPhone(), null);
        }

        //create user
        userObject.setId(null);
        userObject.setStatusCode(LookupCodes.UserStatus.ENABLED);
        if (userObject.getPoint() == null || userObject.getPoint() < 0) {
            userObject.setPoint(0);
        }
        userObject.setCreatedDateTime(DateTime.now());
        UserObject newUserObject = dataAPIClient.post("user", userObject, UserObject.class);

        //force following Yunsu
        userFollowDomain.ensureUserOrganizationFollowing(newUserObject.getId(), Constants.Ids.YUNSU_ORG_ID);

        return newUserObject;
    }

    public Boolean validateUser(String userId) {
        UserAuthentication userAuthentication = tokenAuthenticationService.getAuthentication();
        return userAuthentication != null && userAuthentication.getDetails().getId().equals(userId);
    }


    private void checkPhoneExists(String phone, String userId) {
        UserObject userObject = getUserByPhone(phone);
        if (userObject != null && !userObject.getId().equals(userId)) {
            throw new ConflictException("phone already registered by another user");
        }
    }
}

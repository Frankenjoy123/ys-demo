package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.Constants;
import com.yunsoo.api.rabbit.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.rabbit.file.service.ImageService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.UserConfigObject;
import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.util.ImageProcessor;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.ConflictException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
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
@ObjectCacheConfig
public class UserDomain {

    @Autowired
    private RestClient dataApiClient;

    @Autowired
    private UserFollowDomain userFollowDomain;

    @Autowired
    private ImageService imageService;

    private static final String DEFAULT_GRAVATAR_IMAGE_NAME = "image-400x400";


    @Cacheable(key = "T(com.yunsoo.api.rabbit.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).USER.toString(), #userId)")
    public UserObject getUserById(String userId) {
        try {
            return userId != null ? dataApiClient.get("user/{id}", UserObject.class, userId) : null;
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public UserObject getUserByOpenIdAndType(String openId, String type){
        List<UserObject> userObjects = dataApiClient.get("user?open_id={openId}&oauth_type={type}", new ParameterizedTypeReference<List<UserObject>>() {
        }, openId, type);
        if (userObjects.size() == 0) {
            return null;
        } else {
            return userObjects.get(userObjects.size() - 1);
        }
    }

    public UserObject getUserByPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return null;
        }
        List<UserObject> userObjects = dataApiClient.get("user?phone={phone}", new ParameterizedTypeReference<List<UserObject>>() {
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
        return dataApiClient.get("user?device_id={deviceId}", new ParameterizedTypeReference<List<UserObject>>() {
        }, deviceId);
    }

    @CacheEvict(key = "T(com.yunsoo.api.rabbit.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).USER.toString(), #userObject.getId())")
    public void patchUpdateUser(UserObject userObject) {
        String userId = userObject.getId();
        if (!StringUtils.isEmpty(userId)) {
            //check phone
            if (userObject.getPhone() != null) {
                checkPhoneExists(userObject.getPhone(), userId);
            }
            dataApiClient.patch("user/{id}", userObject, userId);
        }
    }


    public void saveUserGravatar(String userId, byte[] imageDataBytes) {
        String imageName = DEFAULT_GRAVATAR_IMAGE_NAME;
        try {
            ImageProcessor imageProcessor = new ImageProcessor().read(new ByteArrayInputStream(imageDataBytes));
            ByteArrayOutputStream image400x400OutputStream = new ByteArrayOutputStream();
            imageProcessor.resize(400, 400).write(image400x400OutputStream, MediaType.IMAGE_PNG_VALUE);
            String path = String.format("user/%s/gravatar/%s",userId, imageName);
            imageService.save(image400x400OutputStream.toByteArray(),path, MediaType.IMAGE_PNG_VALUE);

        } catch (IOException e) {
            throw new InternalServerErrorException("gravatar upload failed [userId: " + userId + "]");
        }
    }

    public UserObject createUser(UserObject userObject) {
        if (StringUtils.hasText(userObject.getOauthOpenid())) {
            UserObject existingUser = getUserByOpenIdAndType(userObject.getOauthOpenid(), userObject.getOauthTypeCode());
            if (existingUser != null) {
                throw new ConflictException("same openId and type already registered by another user");
            }
        }
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
        UserObject newUserObject = dataApiClient.post("user", userObject, UserObject.class);

        //force following Yunsu
        userFollowDomain.ensureUserOrganizationFollowing(newUserObject.getId(), Constants.Ids.YUNSU_ORG_ID);

        return newUserObject;
    }

    public UserConfigObject getUserConfigByUserId(String userId) {
        try {
            return dataApiClient.get("userConfig/{userId}", UserConfigObject.class, userId);
        } catch (NotFoundException ignored) {
            return defaultUserConfig(userId);
        }
    }

    public void saveUserConfig(UserConfigObject userConfigObject) {
        Assert.hasText(userConfigObject.getUserId());
        userConfigObject.setModifiedDateTime(DateTime.now());
        dataApiClient.put("userConfig/{userId}", userConfigObject, userConfigObject.getUserId());
    }

    private void checkPhoneExists(String phone, String userId) {
        UserObject userObject = getUserByPhone(phone);
        if (userObject != null && !userObject.getId().equals(userId)) {
            throw new ConflictException("phone already registered by another user");
        }
    }

    private UserConfigObject defaultUserConfig(String userId) {
        UserConfigObject object = new UserConfigObject();
        object.setUserId(userId);
        object.setAutoFollowing(true);
        return object;
    }
}

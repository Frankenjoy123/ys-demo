package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserAccessTokenObject;
import com.yunsoo.data.service.entity.UserAccessTokenEntity;
import com.yunsoo.data.service.repository.UserAccessTokenRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Admin on 6/28/2016.
 */
@RestController
@RequestMapping("/userAccessToken")
public class UserAccessTokenController {

    @Autowired
    private UserAccessTokenRepository userAccessTokenRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public UserAccessTokenObject get(@RequestParam(value = "org_id", required = true) String orgId) {

        UserAccessTokenEntity entity = userAccessTokenRepository.findTop1ByOrgIdOrderByCreatedDateTimeDesc(orgId);
        return toUserAccessTokenObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserAccessTokenObject create(@RequestBody @Valid UserAccessTokenObject userAccessTokenObject) {

        if (userAccessTokenObject.getCreatedDateTime() == null) {
            userAccessTokenObject.setCreatedDateTime(DateTime.now());
        }

        userAccessTokenObject.setId(null);
        UserAccessTokenEntity userEventEntity = toUserAccessTokenEntity(userAccessTokenObject);

        UserAccessTokenEntity userAccessTokenEntity = userAccessTokenRepository.save(userEventEntity);
        return toUserAccessTokenObject(userAccessTokenEntity);
    }

    private UserAccessTokenEntity toUserAccessTokenEntity(UserAccessTokenObject object) {
        if (object == null) {
            return null;
        }

        UserAccessTokenEntity entity = new UserAccessTokenEntity();
        entity.setId(object.getId());
        entity.setOrgId(object.getOrgId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setExpiredDatetime(object.getExpiredDatetime());
        entity.setAccessToken(object.getAccessToken());
        entity.setJsapiTicket(object.getJsapiTicket());

        return entity;
    }

    private UserAccessTokenObject toUserAccessTokenObject(UserAccessTokenEntity entity) {
        if (entity == null) {
            return null;
        }

        UserAccessTokenObject object = new UserAccessTokenObject();
        object.setId(entity.getId());
        object.setOrgId(entity.getOrgId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setExpiredDatetime(entity.getExpiredDatetime());
        object.setAccessToken(entity.getAccessToken());
        object.setJsapiTicket(entity.getJsapiTicket());

        return object;
    }
}

package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserTagObject;
import com.yunsoo.data.service.entity.UserTagEntity;
import com.yunsoo.data.service.repository.UserTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Admin on 6/13/2016.
 */
@RestController
@RequestMapping("/user/tag")
public class UserTagController {

    @Autowired
    private UserTagRepository userTagRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserTagObject> getList(@RequestParam(value = "user_id", required = false) String userId,
                                       @RequestParam(value = "ys_id", required = false) String ysId,
                                       @RequestParam(value = "org_id", required = false) String orgId) {

        List<UserTagEntity> userTagEntities = userTagRepository.findByFilter(userId, ysId, orgId);

        return userTagEntities.stream().map(this::toUserTagObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void update(@RequestParam(value = "user_id", required = false) String userId,
                       @RequestParam(value = "ys_id", required = false) String ysId,
                       @RequestParam(value = "org_id", required = false) String orgId,
                       @RequestBody List<UserTagObject> userTagObjects) {

        userTagRepository.deleteByOrgIdAndYsIdAndUserId(orgId, ysId, userId);

        if (userTagObjects == null || userTagObjects.size() == 0)
            return;

        List<UserTagEntity> userTagEntities = new ArrayList<>();
        for (UserTagObject userTagObject : userTagObjects) {
            UserTagEntity userTagEntity = toUserTagEntity(userTagObject);
            userTagEntity.setId(null);
            userTagEntities.add(userTagEntity);
        }

        userTagRepository.save(userTagEntities);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam(value = "user_id", required = false) String userId,
                       @RequestParam(value = "ys_id", required = false) String ysId,
                       @RequestParam(value = "org_id", required = false) String orgId) {

        userTagRepository.deleteByOrgIdAndYsIdAndUserId(orgId, ysId, userId);
    }

    private UserTagObject toUserTagObject(UserTagEntity entity) {
        if (entity == null) {
            return null;
        }

        UserTagObject object = new UserTagObject();
        object.setId(entity.getId());
        object.setUserId(entity.getUserId());
        object.setYsId(entity.getYsId());
        object.setOrgId(entity.getOrgId());
        object.setTagId(entity.getTagId());
        object.setTagName(entity.getTagName());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());

        return object;
    }

    private UserTagEntity toUserTagEntity(UserTagObject object) {
        if (object == null) {
            return null;
        }

        UserTagEntity entity = new UserTagEntity();
        entity.setId(object.getId());
        entity.setUserId(object.getUserId());
        entity.setYsId(object.getYsId());
        entity.setOrgId(object.getOrgId());
        entity.setTagId(object.getTagId());
        entity.setTagName(object.getTagName());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());

        return entity;
    }
}

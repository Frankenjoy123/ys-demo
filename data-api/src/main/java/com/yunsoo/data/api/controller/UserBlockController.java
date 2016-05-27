package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserBlockObject;
import com.yunsoo.data.service.entity.UserBlockEntity;
import com.yunsoo.data.service.repository.UserBlockRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/block")
public class UserBlockController {

    @Autowired
    private UserBlockRepository userBlockRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserBlockObject> queryUserBlock(@RequestParam(value = "user_id", required = false) String userId,
                                                @RequestParam(value = "ys_id", required = false) String ysId,
                                                @RequestParam(value = "org_id", required = false) String orgId) {

        List<UserBlockEntity> entityPage = userBlockRepository.findByFilter(userId, ysId, orgId);

        return entityPage.stream().map(this::toUserBlockObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserBlockObject create(@RequestBody @Valid UserBlockObject userBlockObject) {

        List<UserBlockEntity> entityPage = userBlockRepository.findByFilter(userBlockObject.getUserId(), userBlockObject.getYsId(), userBlockObject.getOrgId());
        if (entityPage == null || entityPage.size() == 0) {
            UserBlockEntity entity = toUserBlockEntity(userBlockObject);
            DateTime now = DateTime.now();

            entity.setId(null);
            if (entity.getCreatedDateTime() == null) {
                entity.setCreatedDateTime(now);
            }

            UserBlockEntity newEntity = userBlockRepository.save(entity);
            return toUserBlockObject(newEntity);
        } else {
            return new UserBlockObject();
        }
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam(value = "user_id", required = false) String userId,
                       @RequestParam(value = "ys_id", required = false) String ysId,
                       @RequestParam(value = "org_id", required = false) String orgId) {

        if (orgId == null || orgId.equals("")) {
            return;
        }

        if ((userId == null || userId.equals("")) && (ysId == null || ysId.equals(""))) {
            return;
        }

        List<UserBlockEntity> entityPage = userBlockRepository.findByFilter(userId, ysId, orgId);
        for (UserBlockEntity entity : entityPage) {
            userBlockRepository.delete(entity.getId());
        }
    }

    private UserBlockObject toUserBlockObject(UserBlockEntity entity) {
        if (entity == null) {
            return null;
        }

        UserBlockObject object = new UserBlockObject();
        object.setId(entity.getId());
        object.setUserId(entity.getUserId());
        object.setYsId(entity.getYsId());
        object.setOrgId(entity.getOrgId());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());

        return object;
    }

    private UserBlockEntity toUserBlockEntity(UserBlockObject object) {
        if (object == null) {
            return null;
        }

        UserBlockEntity entity = new UserBlockEntity();
        entity.setId(object.getId());
        entity.setUserId(object.getUserId());
        entity.setYsId(object.getYsId());
        entity.setOrgId(object.getOrgId());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());

        return entity;
    }

}

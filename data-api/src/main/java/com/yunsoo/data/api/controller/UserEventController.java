package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserEventObject;
import com.yunsoo.data.service.entity.UserEventEntity;
import com.yunsoo.data.service.repository.UserEventRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Admin on 6/27/2016.
 */
@RestController
@RequestMapping("/user/event")
public class UserEventController {

    @Autowired
    private UserEventRepository userEventRepository;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void create(@RequestBody UserEventObject userEventObject) {

        if (userEventObject == null) {
            return;
        }

        if (userEventObject.getCreatedDateTime() == null) {
            userEventObject.setCreatedDateTime(DateTime.now());
        }

        userEventObject.setId(null);
        UserEventEntity userEventEntity = toUserEventEntity(userEventObject);

        userEventRepository.save(userEventEntity);
    }

    private UserEventEntity toUserEventEntity(UserEventObject object) {
        if (object == null) {
            return null;
        }

        UserEventEntity entity = new UserEventEntity();
        entity.setId(object.getId());
        entity.setProductKey(object.getProductKey());
        entity.setScanRecordId(object.getScanRecordId());
        entity.setTypeCode(object.getTypeCode());
        entity.setValue(object.getValue());
        entity.setCreatedDateTime(object.getCreatedDateTime());

        return entity;
    }
}

package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.UserEventDomain;
import com.yunsoo.api.rabbit.dto.UserEvent;
import com.yunsoo.common.data.object.UserEventObject;
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
    private UserEventDomain userEventDomain;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public UserEvent create(@RequestBody UserEvent userEvent) {

        UserEventObject userEventObject = new UserEventObject();

        userEventObject.setId(null);
        userEventObject.setCreatedDateTime(DateTime.now());
        userEventObject.setValue(userEvent.getValue());
        userEventObject.setProductKey(userEvent.getProductKey());
        userEventObject.setTypeCode(userEvent.getTypeCode());
        userEventObject.setScanRecordId(userEvent.getScanRecordId());

        UserEventObject newUserEventObject = userEventDomain.create(userEventObject);

        return new UserEvent(newUserEventObject);
    }
}

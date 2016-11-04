package com.yunsoo.marketing.api.controller;

import com.yunsoo.marketing.dto.UserEvent;
import com.yunsoo.marketing.service.UserEventService;
import com.yunsoo.marketing.service.UserRightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
@RestController
@RequestMapping("/userevent")
public class UserEventController {
    @Autowired
    private UserEventService userEventService;

    @Autowired
    private UserRightService userRightService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserEvent createEvent(@RequestBody @Valid UserEvent userEvent) {
        return userEventService.createUserEvent(userEvent);
    }

}

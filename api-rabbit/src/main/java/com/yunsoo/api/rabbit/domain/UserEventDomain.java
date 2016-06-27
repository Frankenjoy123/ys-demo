package com.yunsoo.api.rabbit.domain;

import com.yunsoo.common.data.object.UserEventObject;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Admin on 6/27/2016.
 */
@Component
public class UserEventDomain {

    @Autowired
    private RestClient dataAPIClient;

    public UserEventObject create(UserEventObject userEventObject) {
        return dataAPIClient.post("user/event", userEventObject, UserEventObject.class);
    }

}

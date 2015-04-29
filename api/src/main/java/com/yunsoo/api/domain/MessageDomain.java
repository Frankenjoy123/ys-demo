package com.yunsoo.api.domain;

import com.yunsoo.api.dto.basic.Message;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by  : Zhe
 * Created on  : 2015/4/28
 * Descriptions:
 */
@Component
public class MessageDomain {

    @Autowired
    private RestClient dataAPIClient;

    public Message getById(Integer id) {
        return dataAPIClient.get("message/{id}", Message.class, id);
    }

}

package com.yunsoo.marketing.service;

import com.yunsoo.marketing.dao.entity.UserEventEntity;
import com.yunsoo.marketing.dao.repository.UserEventRepository;
import com.yunsoo.marketing.dto.UserEvent;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
@Service
public class UserEventService {
    @Autowired
    private UserEventRepository userEventRepository;

    public UserEvent createUserEvent(UserEvent userEvent) {
        UserEventEntity entity = new UserEventEntity();
        entity.setUserId(userEvent.getUserId());
        entity.setMarketingId(userEvent.getMarketingId());
        entity.setEntryId(userEvent.getEntryId());
        entity.setKey(userEvent.getKey());
        entity.setTypeCode(userEvent.getTypeCode());
        entity.setValue(userEvent.getValue());
        entity.setIp(userEvent.getIp());
        entity.setProvince(userEvent.getProvince());
        entity.setCity(userEvent.getCity());
        entity.setUserAgent(userEvent.getUserAgent());
        entity.setCreatedDateTime(DateTime.now());
        return toUserEvent(userEventRepository.save(entity));
    }

    public List<UserEvent> getByKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return new ArrayList<>();
        }
        return userEventRepository.findByKey(key).stream().map(this::toUserEvent).collect(Collectors.toList());
    }


    private UserEvent toUserEvent(UserEventEntity entity) {
        if (entity == null) {
            return null;
        }
        UserEvent userEvent = new UserEvent();
        userEvent.setId(entity.getId());
        userEvent.setUserId(entity.getUserId());
        userEvent.setMarketingId(entity.getMarketingId());
        userEvent.setEntryId(entity.getEntryId());
        userEvent.setKey(entity.getKey());
        userEvent.setTypeCode(entity.getTypeCode());
        userEvent.setValue(entity.getValue());
        userEvent.setIp(entity.getIp());
        userEvent.setProvince(entity.getProvince());
        userEvent.setCity(entity.getCity());
        userEvent.setUserAgent(entity.getUserAgent());
        userEvent.setCreatedDateTime(entity.getCreatedDateTime());
        return userEvent;
    }


    private UserEventEntity toUserEventEntity(UserEvent object) {
        if (object == null) {
            return null;
        }
        UserEventEntity entity = new UserEventEntity();
        entity.setId(object.getId());
        entity.setUserId(object.getUserId());
        entity.setMarketingId(object.getMarketingId());
        entity.setEntryId(object.getEntryId());
        entity.setKey(object.getKey());
        entity.setTypeCode(object.getTypeCode());
        entity.setValue(object.getValue());
        entity.setIp(object.getIp());
        entity.setProvince(object.getProvince());
        entity.setCity(object.getCity());
        entity.setUserAgent(object.getUserAgent());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        return entity;
    }

}

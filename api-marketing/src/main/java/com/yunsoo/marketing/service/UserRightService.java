package com.yunsoo.marketing.service;

import com.yunsoo.common.web.client.Page;
import com.yunsoo.marketing.api.util.PageUtils;
import com.yunsoo.marketing.dao.entity.UserRightEntity;
import com.yunsoo.marketing.dao.repository.UserRightRepository;
import com.yunsoo.marketing.dto.UserRight;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
@Service
public class UserRightService {
    @Autowired
    private UserRightRepository userRightRepository;

    public UserRight createUserRight(UserRight userRight) {
        UserRightEntity entity = new UserRightEntity();
        entity.setUserId(userRight.getUserId());
        entity.setMarketingId(userRight.getMarketingId());
        entity.setMarketingRightId(userRight.getMarketingRightId());
        entity.setUserEventId(userRight.getUserEventId());
        entity.setName(userRight.getName());
        entity.setTypeCode(userRight.getTypeCode());
        entity.setStatusCode(userRight.getStatusCode());
        entity.setAmount(userRight.getAmount());
        entity.setValue(userRight.getValue());
        entity.setDescription(userRight.getDescription());
        entity.setCreatedDateTime(DateTime.now());
        return toUserRight(userRightRepository.save(entity));
    }

    public Long sumUserRight(String marketingId, String marketingRightId) {
        if (StringUtils.isEmpty(marketingId)) {
            return null;
        }
        return userRightRepository.sumUserRightId(marketingId, marketingRightId);
    }

    public Page<UserRight> queryUserMarketing(String marketingId, String marketingRightId, String typeCode, String statusCode, DateTime startTime, DateTime endTime, Pageable pageable) {
        if (StringUtils.isEmpty(marketingId)) {
            return Page.empty();
        }

        return PageUtils.convert(userRightRepository.query(marketingId, marketingRightId, typeCode, statusCode, startTime, endTime, pageable)).map(this::toUserRight);
    }




    private UserRight toUserRight(UserRightEntity entity) {
        if (entity == null) {
            return null;
        }
        UserRight userRight = new UserRight();
        userRight.setId(entity.getId());
        userRight.setUserId(entity.getUserId());
        userRight.setMarketingId(entity.getMarketingId());
        userRight.setMarketingRightId(entity.getMarketingRightId());
        userRight.setUserEventId(entity.getUserEventId());
        userRight.setName(entity.getName());
        userRight.setTypeCode(entity.getTypeCode());
        userRight.setStatusCode(entity.getStatusCode());
        userRight.setAmount(entity.getAmount());
        userRight.setValue(entity.getValue());
        userRight.setDescription(entity.getDescription());
        userRight.setCreatedDateTime(entity.getCreatedDateTime());
        return userRight;
    }


    private UserRightEntity toUserRightEntity(UserRight object) {
        if (object == null) {
            return null;
        }
        UserRightEntity entity = new UserRightEntity();
        entity.setId(object.getId());
        entity.setUserId(object.getUserId());
        entity.setMarketingId(object.getMarketingId());
        entity.setMarketingRightId(object.getMarketingRightId());
        entity.setUserEventId(object.getUserEventId());
        entity.setName(object.getName());
        entity.setTypeCode(object.getTypeCode());
        entity.setStatusCode(object.getStatusCode());
        entity.setAmount(object.getAmount());
        entity.setValue(object.getValue());
        entity.setDescription(object.getDescription());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        return entity;
    }

}

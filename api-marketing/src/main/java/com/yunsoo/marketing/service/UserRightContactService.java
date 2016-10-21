package com.yunsoo.marketing.service;

import com.yunsoo.marketing.dao.entity.UserRightContactEntity;
import com.yunsoo.marketing.dao.repository.UserRightContactRepository;
import com.yunsoo.marketing.dto.UserRightContact;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
@Service
public class UserRightContactService {
    @Autowired
    private UserRightContactRepository userRightContactRepository;

    public UserRightContact createUserRightContact(UserRightContact userRightContact) {
        UserRightContactEntity entity = new UserRightContactEntity();
        entity.setUserRightId(userRightContact.getUserRightId());
        entity.setName(userRightContact.getName());
        entity.setPhone(userRightContact.getPhone());
        entity.setProvince(userRightContact.getProvince());
        entity.setCity(userRightContact.getCity());
        entity.setDistrict(userRightContact.getDistrict());
        entity.setAddress(userRightContact.getAddress());
        entity.setComments(userRightContact.getComments());
        entity.setCreatedDateTime(DateTime.now());
        return toUserRightContact(userRightContactRepository.save(entity));
    }

    public UserRightContact getByUserRightId(String userRightId) {
        if (StringUtils.isEmpty(userRightId)) {
            return null;
        }
        UserRightContactEntity entity = userRightContactRepository.findOne(userRightId);
        return toUserRightContact(entity);
    }

    @Transactional
    public void patchUpdate(UserRightContact userRightContact) {
        if (StringUtils.isEmpty(userRightContact.getUserRightId())) {
            return;
        }
        UserRightContactEntity entity = userRightContactRepository.findOne(userRightContact.getUserRightId());
        if (entity != null) {
            if (userRightContact.getName() != null) entity.setName(userRightContact.getName());
            if (userRightContact.getPhone() != null) entity.setPhone(userRightContact.getPhone());
            if (userRightContact.getProvince() != null) entity.setProvince(userRightContact.getProvince());
            if (userRightContact.getCity() != null) entity.setCity(userRightContact.getCity());
            if (userRightContact.getDistrict() != null) entity.setDistrict(userRightContact.getDistrict());
            if (userRightContact.getAddress() != null) entity.setAddress(userRightContact.getAddress());
            if (userRightContact.getComments() != null) entity.setComments(userRightContact.getComments());
            userRightContactRepository.save(entity);
        }
    }

    private UserRightContact toUserRightContact(UserRightContactEntity entity) {
        if (entity == null) {
            return null;
        }
        UserRightContact userRightContact = new UserRightContact();
        userRightContact.setUserRightId(entity.getUserRightId());
        userRightContact.setName(entity.getName());
        userRightContact.setPhone(entity.getPhone());
        userRightContact.setProvince(entity.getProvince());
        userRightContact.setCity(entity.getCity());
        userRightContact.setDistrict(entity.getDistrict());
        userRightContact.setAddress(entity.getAddress());
        userRightContact.setComments(entity.getComments());
        userRightContact.setCreatedDateTime(entity.getCreatedDateTime());
        return userRightContact;
    }

}

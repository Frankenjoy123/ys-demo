package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserScanRecordObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.UserScanRecordEntity;
import com.yunsoo.data.service.repository.UserScanRecordRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/8/24
 * Descriptions:
 */
@RestController
@RequestMapping("/UserScanRecord")
public class UserScanRecordController {

    @Autowired
    private UserScanRecordRepository userScanRecordRepository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public UserScanRecordObject getById(@PathVariable(value = "id") String id) {
        UserScanRecordEntity entity = userScanRecordRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("user scan record not found by [id: " + id + "]");
        }
        return toUserScanRecordObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserScanRecordObject create(@RequestBody @Valid UserScanRecordObject object) {
        UserScanRecordEntity entity = toUserScanRecordEntity(object);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        UserScanRecordEntity newEntity = userScanRecordRepository.save(entity);
        return toUserScanRecordObject(newEntity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserScanRecordObject> getByFilter(@RequestParam(value = "product_key", required = false) String productKey,
                                                  @RequestParam(value = "user_id", required = false) String userId,
                                                  @SortDefault(sort = "createdDateTime", direction = Sort.Direction.DESC)
                                                  Pageable pageable,
                                                  HttpServletResponse response) {
        Page<UserScanRecordEntity> entityPage;
        if (!StringUtils.isEmpty(productKey)) {
            entityPage = userScanRecordRepository.findByProductKey(productKey, pageable);
        } else if (!StringUtils.isEmpty(userId)) {
            entityPage = userScanRecordRepository.findByUserId(userId, pageable);
        } else {
            throw new BadRequestException("at least one filter parameter required [product_key, user_id]");
        }

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }
        return entityPage.getContent().stream().map(this::toUserScanRecordObject).collect(Collectors.toList());
    }


    private UserScanRecordObject toUserScanRecordObject(UserScanRecordEntity entity) {
        if (entity == null) {
            return null;
        }
        UserScanRecordObject object = new UserScanRecordObject();
        object.setId(entity.getId());
        object.setUserId(entity.getUserId());
        object.setProductKey(entity.getProductKey());
        object.setProductBaseId(entity.getProductBaseId());
        object.setAppId(entity.getAppId());
        object.setDeviceId(entity.getDeviceId());
        object.setLongitude(entity.getLongitude());
        object.setLatitude(entity.getLatitude());
        object.setState(entity.getState());
        object.setCity(entity.getCity());
        object.setAddress(entity.getAddress());
        object.setDetails(entity.getDetails());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        return object;
    }

    private UserScanRecordEntity toUserScanRecordEntity(UserScanRecordObject object) {
        if (object == null) {
            return null;
        }
        UserScanRecordEntity entity = new UserScanRecordEntity();
        entity.setId(object.getId());
        entity.setUserId(object.getUserId());
        entity.setProductKey(object.getProductKey());
        entity.setProductBaseId(object.getProductBaseId());
        entity.setAppId(object.getAppId());
        entity.setDeviceId(object.getDeviceId());
        entity.setLongitude(object.getLongitude());
        entity.setLatitude(object.getLatitude());
        entity.setState(object.getState());
        entity.setCity(object.getCity());
        entity.setAddress(object.getAddress());
        entity.setDetails(object.getDetails());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        return entity;
    }

}

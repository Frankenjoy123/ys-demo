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
@RequestMapping("/userScanRecord")
public class UserScanRecordController {

    @Autowired
    private UserScanRecordRepository userScanRecordRepository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public UserScanRecordObject getById(@PathVariable(value = "id") String id) {
        UserScanRecordEntity entity = userScanRecordRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException(String.format("userScanRecord not found by [id: %s]", id));
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
                                                  @RequestParam(value = "ysid", required = false) String ysid,
                                                  @SortDefault(sort = "createdDateTime", direction = Sort.Direction.ASC)
                                                  Pageable pageable,
                                                  HttpServletResponse response) {
        Page<UserScanRecordEntity> entityPage;
        if (!StringUtils.isEmpty(productKey)) {
            entityPage = userScanRecordRepository.findByProductKey(productKey, pageable);
        } else if (!StringUtils.isEmpty(userId)) {
            entityPage = userScanRecordRepository.findByUserId(userId, pageable);
        } else if (!StringUtils.isEmpty(ysid)) {
            entityPage = userScanRecordRepository.findByYsid(ysid, pageable);
        } else {
            throw new BadRequestException("at least one filter parameter required [product_key, user_id, ysid]");
        }

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }
        return entityPage.getContent().stream().map(this::toUserScanRecordObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "key/{key}", method = RequestMethod.GET)
    public UserScanRecordObject getLatest(@PathVariable(value = "key") String productKey) {
        UserScanRecordEntity entity = userScanRecordRepository.findTop1ByProductKeyOrderByCreatedDateTimeDesc(productKey);
        if (entity == null) {
            throw new NotFoundException(String.format("userScanRecord not found by [id: %s]", productKey));
        }
        return toUserScanRecordObject(entity);
    }



    private UserScanRecordObject toUserScanRecordObject(UserScanRecordEntity entity) {
        if (entity == null) {
            return null;
        }
        UserScanRecordObject object = new UserScanRecordObject();
        object.setId(entity.getId());
        object.setUserId(entity.getUserId());
        object.setProductKey(entity.getProductKey());
        object.setProductKeyBatchId(entity.getProductKeyBatchId());
        object.setProductBaseId(entity.getProductBaseId());
        object.setAppId(entity.getAppId());
        object.setYsid(entity.getYsid());
        object.setDeviceId(entity.getDeviceId());
        object.setIp(entity.getIp());
        object.setLongitude(entity.getLongitude());
        object.setLatitude(entity.getLatitude());
        object.setProvince(entity.getProvince());
        object.setCity(entity.getCity());
        object.setAddress(entity.getAddress());
        object.setDetails(entity.getDetails());
        object.setUserAgent(entity.getUserAgent());
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
        entity.setProductKeyBatchId(object.getProductKeyBatchId());
        entity.setProductBaseId(object.getProductBaseId());
        entity.setAppId(object.getAppId());
        entity.setYsid(object.getYsid());
        entity.setDeviceId(object.getDeviceId());
        entity.setIp(object.getIp());
        entity.setLongitude(object.getLongitude());
        entity.setLatitude(object.getLatitude());
        entity.setProvince(object.getProvince());
        entity.setCity(object.getCity());
        entity.setAddress(object.getAddress());
        entity.setDetails(object.getDetails());
        entity.setUserAgent(object.getUserAgent());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        return entity;
    }

}

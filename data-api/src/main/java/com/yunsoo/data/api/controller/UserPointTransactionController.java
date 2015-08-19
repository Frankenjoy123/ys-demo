package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserPointTransactionObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.UserPointTransactionEntity;
import com.yunsoo.data.service.repository.UserPointTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/8/17
 * Descriptions:
 */
@RestController
@RequestMapping("/userpointtransaction")
public class UserPointTransactionController {

    @Autowired
    private UserPointTransactionRepository userPointTransactionRepository;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public UserPointTransactionObject getById(@PathVariable String id) {
        UserPointTransactionEntity entity = userPointTransactionRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("user_point_transaction not found by [id: " + id + ']');
        }
        return toUserPointTransactionObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserPointTransactionObject> getByFilter(
            @RequestParam(value = "user_id", required = true) String userId,
            Pageable pageable,
            HttpServletResponse response) {
        Page<UserPointTransactionEntity> entityPage = userPointTransactionRepository.findByUserId(userId, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }

        return entityPage.getContent().stream()
                .map(this::toUserPointTransactionObject)
                .collect(Collectors.toList());
    }


    private UserPointTransactionObject toUserPointTransactionObject(UserPointTransactionEntity entity) {
        if (entity == null) {
            return null;
        }
        UserPointTransactionObject object = new UserPointTransactionObject();
        object.setId(entity.getId());
        object.setUserId(entity.getUserId());
        object.setPoint(entity.getPoint());
        object.setIsIncrease(entity.isIncrease());
        object.setTypeCode(entity.getTypeCode());
        object.setStatusCode(entity.getStatusCode());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        return object;
    }

    private UserPointTransactionEntity toUserPointTransactionEntity(UserPointTransactionObject object) {
        if (object == null) {
            return null;
        }
        UserPointTransactionEntity entity = new UserPointTransactionEntity();
        entity.setId(object.getId());
        entity.setUserId(object.getUserId());
        entity.setPoint(object.getPoint());
        entity.setIsIncrease(object.isIncrease());
        entity.setTypeCode(object.getTypeCode());
        entity.setStatusCode(object.getStatusCode());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        return entity;
    }
}

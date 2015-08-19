package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserPointTransactionObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.UserPointTransactionEntity;
import com.yunsoo.data.service.repository.UserPointTransactionRepository;
import com.yunsoo.data.service.service.UserPointTransactionService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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

    @Autowired
    private UserPointTransactionService userPointTransactionService;


    @RequestMapping(value = "{transactionId}", method = RequestMethod.GET)
    public UserPointTransactionObject getById(@PathVariable String transactionId) {
        UserPointTransactionEntity entity = findById(transactionId);
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

    @RequestMapping(value = "", method = RequestMethod.POST)
    public UserPointTransactionObject createTransaction(@RequestBody @Valid UserPointTransactionObject userPointTransactionObject) {
        UserPointTransactionEntity entity = toUserPointTransactionEntity(userPointTransactionObject);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        UserPointTransactionEntity newEntity = userPointTransactionRepository.save(entity);
        return toUserPointTransactionObject(newEntity);
    }

    @RequestMapping(value = "{transactionId}/commit", method = RequestMethod.POST)
    public UserPointTransactionObject commit(@PathVariable String transactionId) {
        UserPointTransactionEntity entity = findById(transactionId);
        UserPointTransactionEntity newEntity = userPointTransactionService.commit(entity);
        return toUserPointTransactionObject(newEntity);
    }

    @RequestMapping(value = "{transactionId}/rollback", method = RequestMethod.POST)
    public UserPointTransactionObject rollback(@PathVariable String transactionId) {
        UserPointTransactionEntity entity = findById(transactionId);
        UserPointTransactionEntity newEntity = userPointTransactionService.rollback(entity);
        return toUserPointTransactionObject(newEntity);
    }

    private UserPointTransactionEntity findById(String transactionId) {
        UserPointTransactionEntity entity = userPointTransactionRepository.findOne(transactionId);
        if (entity == null) {
            throw new NotFoundException("user_point_transaction not found by [id: " + transactionId + ']');
        }
        return entity;
    }

    private UserPointTransactionObject toUserPointTransactionObject(UserPointTransactionEntity entity) {
        if (entity == null) {
            return null;
        }
        UserPointTransactionObject object = new UserPointTransactionObject();
        object.setId(entity.getId());
        object.setUserId(entity.getUserId());
        object.setPoint(entity.getPoint());
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
        entity.setTypeCode(object.getTypeCode());
        entity.setStatusCode(object.getStatusCode());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        return entity;
    }
}

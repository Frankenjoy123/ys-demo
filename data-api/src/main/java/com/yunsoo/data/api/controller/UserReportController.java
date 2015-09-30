package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserReportObject;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.UserReportEntity;
import com.yunsoo.data.service.repository.UserReportRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yan on 9/28/2015.
 */
@RestController
@RequestMapping("/userReport")
public class UserReportController {

    @Autowired
    private UserReportRepository repository;

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public UserReportObject getById(@PathVariable(value = "id") String id){
        return toUserReportObject(repository.findOne(id));
    }


    @RequestMapping(method = RequestMethod.GET)
    public List<UserReportObject> getByFilter(@RequestParam(value = "user_id", required = false)String userId,
                                              @RequestParam(value = "product_base_id_in", required = false)List<String> productBaseIds,
                                              Pageable pageable, HttpServletResponse response){
        Page<UserReportEntity> userReportEntityPage = null;
        if(productBaseIds != null)
            userReportEntityPage = repository.findByProductBaseIdIn(productBaseIds, pageable);
        else if(userId != null )
            userReportEntityPage = repository.findByUserId(userId, pageable);
        if(pageable != null && userReportEntityPage != null)
            response.setHeader("Content-Range", PageableUtils.formatPages(userReportEntityPage.getNumber(), userReportEntityPage.getTotalPages()));

        return userReportEntityPage == null? null : userReportEntityPage.getContent().stream().map(this::toUserReportObject).collect(Collectors.toList());

    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserReportObject saveUserReport(@RequestBody UserReportObject object){
        if(object!=null) {
            UserReportEntity entity = toUserReportEntity(object);
            entity.setCreatedDateTime(DateTime.now());
            repository.save(entity);
            return toUserReportObject(entity);
        }

        return null;
    }

    private UserReportEntity toUserReportEntity(UserReportObject object){
        UserReportEntity entity =new UserReportEntity();
        entity.setUserId(object.getUserId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setId(object.getId());
        entity.setProductBaseId(object.getProductBaseId());
        entity.setReportDetails(object.getReportDetails());
        entity.setStoreAddress(object.getStoreAddress());
        entity.setStoreName(object.getStoreName());
        return entity;
    }

    private UserReportObject toUserReportObject(UserReportEntity entity){
        UserReportObject object =new UserReportObject();
        object.setUserId(entity.getUserId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setId(entity.getId());
        object.setProductBaseId(entity.getProductBaseId());
        object.setReportDetails(entity.getReportDetails());
        object.setStoreAddress(entity.getStoreAddress());
        object.setStoreName(entity.getStoreName());
        return object;
    }
}

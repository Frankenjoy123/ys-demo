package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.OperationLogObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.OperationLogEntity;
import com.yunsoo.data.service.repository.OperationLogRepository;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yan on 7/6/2016.
 */
@RestController
@RequestMapping("operation")
public class OperationLogController {

    @Autowired
    private OperationLogRepository repository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public OperationLogObject getById(@PathVariable("id")String id){
        OperationLogEntity entity = repository.findOne(id);
        if(entity == null)
            throw new NotFoundException("Operation log not exist with id: " + id);

        return toOperationLogObject(entity);
    }

    @RequestMapping(method = RequestMethod.POST)
    public OperationLogObject save(@RequestBody OperationLogObject log, @RequestHeader(value = "User-Agent", required = false) String userAgent){
        OperationLogEntity entity = toOperationLogEntity(log);
        entity.setUserAgent(userAgent);
        entity.setId(null);
        if(entity.getCreatedDateTime() == null)
            entity.setCreatedDateTime(DateTime.now());
        repository.save(entity);
        return toOperationLogObject(entity);
    }



    private OperationLogObject toOperationLogObject(OperationLogEntity entity){
        OperationLogObject object = new OperationLogObject();
        BeanUtils.copyProperties(entity, object);
        return object;

    }

    private OperationLogEntity toOperationLogEntity(OperationLogObject object){
        OperationLogEntity entity = new OperationLogEntity();
        BeanUtils.copyProperties(object, entity);
        return entity;
    }

}

package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.OperationLogObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.OperationLogEntity;
import com.yunsoo.data.service.repository.OperationLogRepository;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

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

    @RequestMapping( method = RequestMethod.GET)
    public List<OperationLogObject> filter(@RequestParam("account_ids")List<String> accountIds,
                                           @RequestParam(value = "app_id", required = false)String appId,
                                           @RequestParam(value = "operation", required = false)String operation,
                                           @RequestParam(value = "create_datetime_start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime start,
                                           @RequestParam(value = "create_datetime_end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime end,
                                           @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)Pageable pageable,
                                           HttpServletResponse response){
        if(start == null)
            start = DateTime.now().minusMonths(3);
        Page<OperationLogEntity> entityPage = repository.query(accountIds, appId, operation, start, end, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }

        return entityPage.map(this::toOperationLogObject).getContent();

    }


    @RequestMapping(method = RequestMethod.POST)
    public OperationLogObject save(@RequestBody OperationLogObject log){
        OperationLogEntity entity = toOperationLogEntity(log);
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

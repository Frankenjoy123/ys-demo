package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.LogisticsCheckActionObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.LogisticsCheckActionEntity;
import com.yunsoo.data.service.repository.LogisticsCheckActionRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen Jerry on 3/4/2015.
 */
@RestController
@RequestMapping("/logisticscheckaction")
public class LogisticsCheckActionController {

    @Autowired
    private LogisticsCheckActionRepository logisticsCheckActionRepository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public LogisticsCheckActionObject get(@PathVariable(value = "id") Integer id) {

        LogisticsCheckActionEntity logisticsCheckActionEntity = logisticsCheckActionRepository.findOne(id);
        if (logisticsCheckActionEntity == null) {
            throw new NotFoundException("logisticsCheckActionEntity not found by [id: " + id + "]");
        }

        LogisticsCheckActionObject logisticsCheckActionObject = new LogisticsCheckActionObject();
        BeanUtils.copyProperties(logisticsCheckActionEntity, logisticsCheckActionObject);

        return logisticsCheckActionObject;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<LogisticsCheckActionObject> get(@RequestParam(value = "orgId", required = true) String orgId,
                                       @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        Iterable<LogisticsCheckActionEntity> entityList = null;
        entityList = logisticsCheckActionRepository.findByOrgId(orgId, new PageRequest(pageIndex, pageSize));

        if (entityList == null) {
            throw new NotFoundException("LogisticsCheckActionObject");
        }

        List<LogisticsCheckActionObject> actionObjects = new ArrayList<>();
        for (LogisticsCheckActionEntity entity : entityList) {
            LogisticsCheckActionObject actionObject = new LogisticsCheckActionObject();
            BeanUtils.copyProperties(entity, actionObject);
            actionObjects.add(actionObject);
        }

        return actionObjects;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public LogisticsCheckActionObject create(@RequestBody LogisticsCheckActionObject action) {

        LogisticsCheckActionEntity logisticsCheckActionEntity = new LogisticsCheckActionEntity();
        BeanUtils.copyProperties(action, logisticsCheckActionEntity);

        LogisticsCheckActionEntity newEntity = logisticsCheckActionRepository.save(logisticsCheckActionEntity);

        LogisticsCheckActionObject logisticsCheckActionObject = new LogisticsCheckActionObject();
        BeanUtils.copyProperties(newEntity, logisticsCheckActionObject);

        return logisticsCheckActionObject;
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public LogisticsCheckActionObject update(@RequestBody LogisticsCheckActionObject action) {

        LogisticsCheckActionEntity logisticsCheckActionEntity = new LogisticsCheckActionEntity();
        BeanUtils.copyProperties(action, logisticsCheckActionEntity);

        LogisticsCheckActionEntity newEntity = logisticsCheckActionRepository.save(logisticsCheckActionEntity);

        LogisticsCheckActionObject logisticsCheckActionObject = new LogisticsCheckActionObject();
        BeanUtils.copyProperties(newEntity, logisticsCheckActionObject);

        return logisticsCheckActionObject;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value = "id") Integer id) {

        logisticsCheckActionRepository.delete(id);
    }
}

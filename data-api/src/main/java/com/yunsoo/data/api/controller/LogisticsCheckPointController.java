package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.LogisticsCheckPointObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.api.dto.ResultWrapper;
import com.yunsoo.data.api.factory.ResultFactory;
import com.yunsoo.data.service.entity.LogisticsCheckPointEntity;
import com.yunsoo.data.service.entity.OrganizationEntity;
import com.yunsoo.data.service.repository.LogisticsCheckPointRepository;
import com.yunsoo.data.service.service.ServiceOperationStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen Jerry on 3/12/2015.
 */
@RestController
@RequestMapping("/logisticscheckpoint")
public class LogisticsCheckPointController {

    @Autowired
    LogisticsCheckPointRepository logisticsCheckPointRepository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public LogisticsCheckPointObject getLogisticsCheckPointById(@PathVariable(value = "id") String id) {

        LogisticsCheckPointEntity logisticsCheckPointEntity = logisticsCheckPointRepository.findOne(id);
        if (logisticsCheckPointEntity == null) {
            throw new NotFoundException("logisticsCheckPointEntity not found by [id: " + id + "]");
        }

        LogisticsCheckPointObject logisticsCheckPointObject = new LogisticsCheckPointObject();
        BeanUtils.copyProperties(logisticsCheckPointEntity, logisticsCheckPointObject);

        return logisticsCheckPointObject;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<LogisticsCheckPointObject> get(@RequestParam(value = "orgId", required = true) String orgId,
                                                @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
                                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        Iterable<LogisticsCheckPointEntity> entityList = null;
        entityList = logisticsCheckPointRepository.findByOrgId(orgId, new PageRequest(pageIndex, pageSize));

        if (entityList == null) {
            throw new NotFoundException("LogisticsCheckPointObject");
        }

        List<LogisticsCheckPointObject> pointObjects = new ArrayList<>();
        for (LogisticsCheckPointEntity entity : entityList) {
            LogisticsCheckPointObject pointObject = new LogisticsCheckPointObject();
            BeanUtils.copyProperties(entity, pointObject);
            pointObjects.add(pointObject);
        }

        return pointObjects;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public LogisticsCheckPointObject create(@RequestBody LogisticsCheckPointObject logisticsCheckPointObject) {

        LogisticsCheckPointEntity logisticsCheckPointEntity = new LogisticsCheckPointEntity();
        BeanUtils.copyProperties(logisticsCheckPointObject, logisticsCheckPointEntity);

        LogisticsCheckPointEntity newEntity = logisticsCheckPointRepository.save(logisticsCheckPointEntity);

        LogisticsCheckPointObject newObject = new LogisticsCheckPointObject();
        BeanUtils.copyProperties(newEntity, newObject);

        return newObject;
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public LogisticsCheckPointObject update(@RequestBody LogisticsCheckPointObject logisticsCheckPointObject) {

        LogisticsCheckPointEntity logisticsCheckPointEntity = new LogisticsCheckPointEntity();
        BeanUtils.copyProperties(logisticsCheckPointObject, logisticsCheckPointEntity);

        LogisticsCheckPointEntity newEntity = logisticsCheckPointRepository.save(logisticsCheckPointEntity);

        LogisticsCheckPointObject newObject = new LogisticsCheckPointObject();
        BeanUtils.copyProperties(newEntity, newObject);

        return newObject;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value = "id") String id) {

        logisticsCheckPointRepository.delete(id);
    }
}

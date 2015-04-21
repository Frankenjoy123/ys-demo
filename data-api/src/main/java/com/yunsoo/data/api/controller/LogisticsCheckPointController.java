package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.LogisticsCheckPointObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.api.dto.ResultWrapper;
import com.yunsoo.data.api.factory.ResultFactory;
import com.yunsoo.data.service.entity.LogisticsCheckPointEntity;
import com.yunsoo.data.service.entity.OrganizationEntity;
import com.yunsoo.data.service.repository.LogisticsCheckPointRepository;
import com.yunsoo.data.service.service.LogisticsCheckPointService;
import com.yunsoo.data.service.service.ServiceOperationStatus;
import com.yunsoo.data.service.service.contract.LogisticsCheckPoint;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Chen Jerry on 3/12/2015.
 */
@RestController
@RequestMapping("/logisticscheckpoint")
public class LogisticsCheckPointController {

    @Autowired
    LogisticsCheckPointRepository logisticsCheckPointRepository;

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public LogisticsCheckPointObject getLogisticsCheckPointById(@PathVariable(value = "id") String id) {

        LogisticsCheckPointEntity logisticsCheckPointEntity = logisticsCheckPointRepository.findOne(id);
        if (logisticsCheckPointEntity == null) {
            throw new NotFoundException("logisticsCheckPointEntity not found by [id: " + id + "]");
        }

        LogisticsCheckPointObject logisticsCheckPointObject = new LogisticsCheckPointObject();
        BeanUtils.copyProperties(logisticsCheckPointEntity, logisticsCheckPointObject);

        return logisticsCheckPointObject;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void createLogisticsCheckPoint(@RequestBody LogisticsCheckPointObject logisticsCheckPointObject) {

        LogisticsCheckPointEntity logisticsCheckPointEntity = new LogisticsCheckPointEntity();
        BeanUtils.copyProperties(logisticsCheckPointObject, logisticsCheckPointEntity);

        logisticsCheckPointRepository.save(logisticsCheckPointEntity);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void updateLogisticsCheckPoint(@RequestBody LogisticsCheckPointObject logisticsCheckPointObject) {

        LogisticsCheckPointEntity logisticsCheckPointEntity = new LogisticsCheckPointEntity();
        BeanUtils.copyProperties(logisticsCheckPointObject, logisticsCheckPointEntity);

        logisticsCheckPointRepository.save(logisticsCheckPointEntity);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteLogisticsCheckPoint(@PathVariable(value = "id") String id) {

        logisticsCheckPointRepository.delete(id);
    }
}

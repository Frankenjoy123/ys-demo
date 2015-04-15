package com.yunsoo.data.api.controller;

import com.yunsoo.data.api.dto.ResultWrapper;
import com.yunsoo.data.api.factory.ResultFactory;
import com.yunsoo.data.service.service.LogisticsCheckPointService;
import com.yunsoo.data.service.service.ServiceOperationStatus;
import com.yunsoo.data.service.service.contract.LogisticsCheckPoint;
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
    private final LogisticsCheckPointService pointService;

    @Autowired
    LogisticsCheckPointController(LogisticsCheckPointService pointService) {
        this.pointService = pointService;
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<LogisticsCheckPoint> getLogisticsCheckPointById(@PathVariable(value = "id") int id) {
        return new ResponseEntity<LogisticsCheckPoint>(pointService.get(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<LogisticsCheckPoint> getLogisticsCheckPointByName(@PathVariable(value = "name") String name) {
        return new ResponseEntity<LogisticsCheckPoint>(pointService.get(name), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResultWrapper> createLogisticsCheckPoint(@RequestBody LogisticsCheckPoint point) {
        int id = pointService.save(point);
        HttpStatus status = id > 0 ? HttpStatus.CREATED : HttpStatus.UNPROCESSABLE_ENTITY;
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(id), status);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResponseEntity<ResultWrapper> updateLogisticsCheckPoint(@RequestBody LogisticsCheckPoint point) {
        Boolean result = pointService.update(point).equals(ServiceOperationStatus.Success) ? true : false;
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(result), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResultWrapper> deleteLogisticsCheckPoint(@PathVariable(value = "id") int id) {
        Boolean result = pointService.delete(id, 5); //deletePermanantly status is 5 in dev DB
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(result), HttpStatus.NO_CONTENT);
    }
}

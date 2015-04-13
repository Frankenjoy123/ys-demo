package com.yunsoo.data.api.controller;

import com.yunsoo.data.api.dto.ResultWrapper;
import com.yunsoo.data.api.factory.ResultFactory;
import com.yunsoo.data.service.service.LogisticsCheckActionService;
import com.yunsoo.data.service.service.ServiceOperationStatus;

import com.yunsoo.data.service.service.contract.LogisticsCheckAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Chen Jerry on 3/4/2015.
 */
@RestController
@RequestMapping("/logisticscheckaction")
public class LogisticsCheckActionController {

    @Autowired
    private final LogisticsCheckActionService actionService;

    @Autowired
    LogisticsCheckActionController(LogisticsCheckActionService actionService) {
        this.actionService = actionService;
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<LogisticsCheckAction> getLogisticsCheckActionById(@PathVariable(value = "id") int id) {
        return new ResponseEntity<LogisticsCheckAction>(actionService.get(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<LogisticsCheckAction> getLogisticsCheckActionByName(@PathVariable(value = "name") String name) {
        return new ResponseEntity<LogisticsCheckAction>(actionService.get(name), HttpStatus.OK);
    }

    @RequestMapping(value = "/org/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<LogisticsCheckAction>> getLogisticsCheckActionByOrg(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<List<LogisticsCheckAction>>(actionService.getLogisticsCheckActionsByOrg(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<ResultWrapper> createLogisticsCheckAction(@RequestBody LogisticsCheckAction action) {
        int id = actionService.save(action);
        HttpStatus status = id > 0 ? HttpStatus.CREATED : HttpStatus.UNPROCESSABLE_ENTITY;
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(id), status);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<ResultWrapper> updateLogisticsCheckAction(@RequestBody LogisticsCheckAction action) {
        Boolean result = actionService.update(action).equals(ServiceOperationStatus.Success) ? true : false;
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(result), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResultWrapper> deleteLogisticsCheckAction(@PathVariable(value = "id") int id) {
        Boolean result = actionService.delete(id, 5); //deletePermanantly status is 5 in dev DB
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(result), HttpStatus.NO_CONTENT);
    }
}

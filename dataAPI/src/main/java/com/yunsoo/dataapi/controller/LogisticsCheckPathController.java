package com.yunsoo.dataapi.controller;

import com.yunsoo.dataapi.dto.ResultWrapper;
import com.yunsoo.dataapi.factory.ResultFactory;
import com.yunsoo.service.LogisticsCheckPathService;
import com.yunsoo.service.ServiceOperationStatus;
import com.yunsoo.service.contract.LogisticsCheckPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Chen Jerry on 3/4/2015.
 */
@RestController
@RequestMapping("/logisticscheckpath")
public class LogisticsCheckPathController {

    @Autowired
    private final LogisticsCheckPathService pathService;

    @Autowired
    LogisticsCheckPathController(LogisticsCheckPathService pathService) {
        this.pathService = pathService;
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<LogisticsCheckPath> getLogisticsCheckPathById(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<LogisticsCheckPath>(pathService.get(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/productKey/{productKey}", method = RequestMethod.GET)
    public ResponseEntity<List<LogisticsCheckPath>> getLogisticsCheckPathsOrderByStartDate(@PathVariable(value = "productKey") String productKey) {
        List<LogisticsCheckPath> pathList = pathService.getLogisticsCheckPathsOrderByStartDate(productKey);
        return new ResponseEntity<List<LogisticsCheckPath>>(pathList, HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<ResultWrapper> createLogisticsCheckPath(@RequestBody LogisticsCheckPath path) {
        Long id = pathService.save(path);
        HttpStatus status = id > 0 ? HttpStatus.CREATED : HttpStatus.UNPROCESSABLE_ENTITY;
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(id), status);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<ResultWrapper> updateLogisticsCheckPath(@RequestBody LogisticsCheckPath path) {
        Boolean result = pathService.update(path).equals(ServiceOperationStatus.Success) ? true : false;
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(result), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResultWrapper> deleteLogisticsCheckPath(@PathVariable(value = "id") Long id) {
        Boolean result = pathService.delete(id, 5); //delete status is 5 in dev DB
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(result), HttpStatus.NO_CONTENT);
    }
}

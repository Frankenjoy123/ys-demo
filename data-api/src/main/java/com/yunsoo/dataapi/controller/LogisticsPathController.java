package com.yunsoo.dataapi.controller;

import com.yunsoo.common.data.object.LogisticsPathObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
import com.yunsoo.service.LogisticsPathService;
import com.yunsoo.service.ProductPackageService;
import com.yunsoo.service.contract.LogisticsPath;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerry on 3/23/2015.
 */
@RestController
@RequestMapping("/logisticspath")
public class LogisticsPathController {

    @Autowired
    private LogisticsPathService pathService;

    @Autowired
    private ProductPackageService packageService;

    @RequestMapping(value = "/key/{key}", method = RequestMethod.GET)
    public List<LogisticsPathObject> get(@PathVariable(value = "key") String key) {

        if (key == null || key.isEmpty())
            throw new NotFoundException("LogisticsPathObject");

        List<LogisticsPath> logisticsPathList = pathService.get(key);
        if (logisticsPathList == null)
            throw new NotFoundException("LogisticsPathObject");

        List<LogisticsPathObject> logisticsPathObjectList = new ArrayList<LogisticsPathObject>();
        for (LogisticsPath logisticsPath : logisticsPathList) {
            LogisticsPathObject logisticsPathObject = new LogisticsPathObject();
            logisticsPathObject.setProductKey(logisticsPath.getProductKey());
            logisticsPathObject.setStartCheckPoint(logisticsPath.getStartCheckPoint());
            logisticsPathObject.setStartDate(logisticsPath.getStartDate());
            logisticsPathObject.setEndCheckPoint(logisticsPath.getEndCheckPoint());
            logisticsPathObject.setEndDate(logisticsPath.getEndDate());
            logisticsPathObject.setOperator(logisticsPath.getOperator());
            logisticsPathObject.setAction_id(logisticsPath.getAction_id());
            logisticsPathObject.setDesc(logisticsPath.getDesc());

            logisticsPathObjectList.add(logisticsPathObject);
        }

        return logisticsPathObjectList;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void create(@RequestBody LogisticsPathObject logisticsPathObject) {

        if (logisticsPathObject == null)
            throw new NotFoundException("LogisticsPathObject");

        List<String> allKeys = packageService.loadAllKeys(logisticsPathObject.getProductKey());
        if (allKeys == null)
            throw new UnprocessableEntityException();

        List<LogisticsPathObject> logisticsPathObjectList = new ArrayList<LogisticsPathObject>();
        for (String key : allKeys) {
            LogisticsPathObject tmpPathDto = new LogisticsPathObject();
            BeanUtils.copyProperties(logisticsPathObject, tmpPathDto);
            tmpPathDto.setProductKey(key);

            logisticsPathObjectList.add(tmpPathDto);
        }

        List<LogisticsPath> logisticsPathList = new ArrayList<LogisticsPath>();
        for (LogisticsPathObject pathObject : logisticsPathObjectList) {
            LogisticsPath path = new LogisticsPath();
            path.setProductKey(pathObject.getProductKey());
            path.setStartCheckPoint(pathObject.getStartCheckPoint());

            //Use the sever time
            path.setStartDate(DateTime.now());
            path.setEndCheckPoint(pathObject.getEndCheckPoint());
            path.setEndDate(pathObject.getEndDate());
            path.setOperator(pathObject.getOperator());
            path.setAction_id(pathObject.getAction_id());
            path.setDesc(pathObject.getDesc());

            logisticsPathList.add(path);
        }

        pathService.save(logisticsPathList);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void update(@RequestBody LogisticsPathObject logisticsPathObject) {

        if (logisticsPathObject == null)
            throw new NotFoundException("LogisticsPathObject");

        LogisticsPath path = new LogisticsPath();
        path.setProductKey(logisticsPathObject.getProductKey());
        path.setStartCheckPoint(logisticsPathObject.getStartCheckPoint());
        path.setStartDate(logisticsPathObject.getStartDate());
        path.setEndCheckPoint(logisticsPathObject.getEndCheckPoint());
        path.setEndDate(logisticsPathObject.getEndDate());
        path.setOperator(logisticsPathObject.getOperator());
        path.setAction_id(logisticsPathObject.getAction_id());
        path.setDesc(logisticsPathObject.getDesc());

        pathService.update(path);
    }
}
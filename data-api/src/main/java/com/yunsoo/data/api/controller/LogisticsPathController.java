package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.LogisticsBatchPathObject;
import com.yunsoo.common.data.object.LogisticsPathObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.service.LogisticsPathService;
import com.yunsoo.data.service.service.ProductPackageService;
import com.yunsoo.data.service.service.contract.LogisticsPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by  : Jerry
 * Created on  : 3/23/2015
 * Descriptions:
 */
@RestController
@RequestMapping("/logisticspath")
public class LogisticsPathController {

    @Autowired
    private LogisticsPathService pathService;

    @Autowired
    private ProductPackageService packageService;

    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public List<LogisticsPathObject> get(@PathVariable(value = "key") String key) {

        List<LogisticsPath> logisticsPathList = pathService.getByProductKey(key);
        if (logisticsPathList == null) {
            return null;
        }
        return logisticsPathList.stream().map(this::toLogisticsPathObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void create(@RequestBody LogisticsPathObject logisticsPathObject) {
        List<String> allKeys = new ArrayList<>();
        try {
            Set<String> itemKeys = packageService.getAllChildProductKeySetByKey(logisticsPathObject.getProductKey());
            allKeys.addAll(allKeys.size(), itemKeys);
        } catch (IllegalArgumentException ex) {
            allKeys.add(logisticsPathObject.getProductKey());
        }

        List<LogisticsPath> logisticsPathList = allKeys.stream().map(k -> {
            LogisticsPath path = toLogisticsPath(logisticsPathObject);
            path.setProductKey(k);
            return path;
        }).collect(Collectors.toList());

        pathService.save(logisticsPathList);
    }

    @RequestMapping(value = "batch", method = RequestMethod.POST)
    public void batchCreate(@RequestBody LogisticsBatchPathObject logisticsBatchPathObject) {
        if (logisticsBatchPathObject != null && logisticsBatchPathObject.getProductKeys() != null) {
            List<String> allKeys = new ArrayList<>();
            for (String key : logisticsBatchPathObject.getProductKeys()) {
                try {
                    Set<String> itemKeys = packageService.getAllChildProductKeySetByKey(key);
                    allKeys.addAll(allKeys.size(), itemKeys);
                } catch (IllegalArgumentException ex) {
                    allKeys.add(key);
                }
            }
            List<LogisticsPath> logisticsPathList = allKeys.stream().map(k -> {
                LogisticsPath path = new LogisticsPath();
                path.setProductKey(k);
                path.setStartCheckPoint(logisticsBatchPathObject.getStartCheckPoint());
                path.setStartDateTime(logisticsBatchPathObject.getStartDateTime());
                path.setEndCheckPoint(logisticsBatchPathObject.getEndCheckPoint());
                path.setEndDateTime(logisticsBatchPathObject.getEndDateTime());
                path.setOperator(logisticsBatchPathObject.getOperator());
                path.setActionId(logisticsBatchPathObject.getActionId());
                path.setDeviceId(logisticsBatchPathObject.getDeviceId());
                path.setDescription(logisticsBatchPathObject.getDescription());
                return path;
            }).collect(Collectors.toList());

            pathService.save(logisticsPathList);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void update(@RequestBody LogisticsPathObject logisticsPathObject) {
        if (logisticsPathObject == null)
            throw new NotFoundException("LogisticsPathObject");

        LogisticsPath path = new LogisticsPath();
        path.setProductKey(logisticsPathObject.getProductKey());
        path.setActionId(logisticsPathObject.getActionId());
        path.setStartCheckPoint(logisticsPathObject.getStartCheckPoint());
        path.setStartDateTime(logisticsPathObject.getStartDateTime());
        path.setEndCheckPoint(logisticsPathObject.getEndCheckPoint());
        path.setEndDateTime(logisticsPathObject.getEndDateTime());
        path.setOperator(logisticsPathObject.getOperator());
        path.setDeviceId(logisticsPathObject.getDeviceId());
        path.setDescription(logisticsPathObject.getDescription());

        pathService.update(path);
    }


    private LogisticsPathObject toLogisticsPathObject(LogisticsPath logisticsPath) {
        if (logisticsPath == null) {
            return null;
        }
        LogisticsPathObject logisticsPathObject = new LogisticsPathObject();
        logisticsPathObject.setProductKey(logisticsPath.getProductKey());
        logisticsPathObject.setActionId(logisticsPath.getActionId());
        logisticsPathObject.setStartCheckPoint(logisticsPath.getStartCheckPoint());
        logisticsPathObject.setStartDateTime(logisticsPath.getStartDateTime());
        logisticsPathObject.setEndCheckPoint(logisticsPath.getEndCheckPoint());
        logisticsPathObject.setEndDateTime(logisticsPath.getEndDateTime());
        logisticsPathObject.setOperator(logisticsPath.getOperator());
        logisticsPathObject.setDeviceId(logisticsPath.getDeviceId());
        logisticsPathObject.setDescription(logisticsPath.getDescription());
        return logisticsPathObject;
    }

    private LogisticsPath toLogisticsPath(LogisticsPathObject logisticsPathObject) {
        if (logisticsPathObject == null) {
            return null;
        }
        LogisticsPath logisticsPath = new LogisticsPath();
        logisticsPath.setProductKey(logisticsPathObject.getProductKey());
        logisticsPath.setActionId(logisticsPathObject.getActionId());
        logisticsPath.setStartCheckPoint(logisticsPathObject.getStartCheckPoint());
        logisticsPath.setStartDateTime(logisticsPathObject.getStartDateTime());
        logisticsPath.setEndCheckPoint(logisticsPathObject.getEndCheckPoint());
        logisticsPath.setEndDateTime(logisticsPathObject.getEndDateTime());
        logisticsPath.setOperator(logisticsPathObject.getOperator());
        logisticsPath.setDeviceId(logisticsPathObject.getDeviceId());
        logisticsPath.setDescription(logisticsPathObject.getDescription());
        return logisticsPath;
    }
}

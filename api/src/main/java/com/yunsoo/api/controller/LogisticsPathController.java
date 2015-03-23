package com.yunsoo.api.controller;

import com.yunsoo.api.biz.LogisticsDomain;
import com.yunsoo.api.dto.LogisticsPath;
import com.yunsoo.common.data.object.LogisticsCheckActionObject;
import com.yunsoo.common.data.object.LogisticsCheckPathObject;
import com.yunsoo.common.data.object.LogisticsCheckPointObject;
import com.yunsoo.common.data.object.LogisticsPathObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jerry on 3/16/2015.
 */
@RestController
@RequestMapping("/logistics")
public class LogisticsPathController {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private LogisticsDomain logisticsDomain;

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public List<LogisticsPath> get(@PathVariable(value = "key") String key) {
        return logisticsDomain.getLogisticsPathsOrderByStartDate(key);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody LogisticsPathObject logisticsPathObject) {
        dataAPIClient.post("logisticspath/create", logisticsPathObject, Long.class);
    }
}

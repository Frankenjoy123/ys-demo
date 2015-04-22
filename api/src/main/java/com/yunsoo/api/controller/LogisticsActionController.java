package com.yunsoo.api.controller;

import com.yunsoo.common.data.object.LogisticsCheckActionObject;
import com.yunsoo.common.data.object.LogisticsPathObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Jerry on 3/24/2015.
 */
@RestController
@RequestMapping("/logisticsaction")
public class LogisticsActionController {

    @Autowired
    private RestClient dataAPIClient;

    @RequestMapping(value = "/org/{id}", method = RequestMethod.GET)
    public List<LogisticsCheckActionObject> get(@PathVariable(value = "id") Long id) {

        LogisticsCheckActionObject[] logisticsCheckActionObjects = dataAPIClient.get("logisticscheckaction/org/{id}", LogisticsCheckActionObject[].class, id);
        if(logisticsCheckActionObjects == null || logisticsCheckActionObjects.length == 0)
            throw new NotFoundException("Logistics action not found orgId=" + id);

        List<LogisticsCheckActionObject> logisticsCheckActionObjectList = Arrays.asList(logisticsCheckActionObjects);
        return logisticsCheckActionObjectList;
    }
}

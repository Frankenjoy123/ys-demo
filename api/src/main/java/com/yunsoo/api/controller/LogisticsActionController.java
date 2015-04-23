package com.yunsoo.api.controller;

import com.yunsoo.common.data.object.LogisticsCheckActionObject;
import com.yunsoo.common.data.object.LogisticsPathObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody LogisticsCheckActionObject logisticsCheckActionObject) {
        dataAPIClient.post("logisticscheckaction", logisticsCheckActionObject, Long.class);
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public LogisticsCheckActionObject get(@PathVariable(value = "id") int id) {

        LogisticsCheckActionObject logisticsCheckActionObject = dataAPIClient.get("logisticscheckaction/id/{id}", LogisticsCheckActionObject.class, id);
        if(logisticsCheckActionObject == null)
            throw new NotFoundException("Logistics action not found id=" + id);

        return logisticsCheckActionObject;
    }

    @RequestMapping(value = "/org/{id}", method = RequestMethod.GET)
    public List<LogisticsCheckActionObject> get(@PathVariable(value = "id") Long id) {

        LogisticsCheckActionObject[] logisticsCheckActionObjects = dataAPIClient.get("logisticscheckaction/org/{id}", LogisticsCheckActionObject[].class, id);
        if(logisticsCheckActionObjects == null || logisticsCheckActionObjects.length == 0)
            throw new NotFoundException("Logistics action not found orgId=" + id);

        List<LogisticsCheckActionObject> logisticsCheckActionObjectList = Arrays.asList(logisticsCheckActionObjects);
        return logisticsCheckActionObjectList;
    }
}

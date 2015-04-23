package com.yunsoo.api.controller;

import com.yunsoo.common.data.object.LogisticsCheckPointObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Jerry on 3/24/2015.
 */
@RestController
@RequestMapping("/logisticspoint")
public class LogisticsPointController {

    @Autowired
    private RestClient dataAPIClient;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody LogisticsCheckPointObject logisticsCheckPointObject) {
        dataAPIClient.post("logisticscheckpoint", logisticsCheckPointObject, Long.class);
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public LogisticsCheckPointObject get(@PathVariable(value = "id") String id) {

        LogisticsCheckPointObject logisticsCheckPointObject = dataAPIClient.get("logisticscheckpoint/id/{id}", LogisticsCheckPointObject.class, id);
        if(logisticsCheckPointObject == null)
            throw new NotFoundException("Logistics point not found id=" + id);

        return logisticsCheckPointObject;
    }
}

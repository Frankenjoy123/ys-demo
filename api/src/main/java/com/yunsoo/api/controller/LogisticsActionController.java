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
    public LogisticsCheckActionObject create(@RequestBody LogisticsCheckActionObject logisticsCheckActionObject) {

        LogisticsCheckActionObject newObject = dataAPIClient.post("logisticscheckaction", logisticsCheckActionObject, LogisticsCheckActionObject.class);
        return newObject;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public LogisticsCheckActionObject get(@PathVariable(value = "id") Integer id) {

        LogisticsCheckActionObject logisticsCheckActionObject = dataAPIClient.get("logisticscheckaction/{id}", LogisticsCheckActionObject.class, id);
        if(logisticsCheckActionObject == null)
            throw new NotFoundException("Logistics action not found id=" + id);

        return logisticsCheckActionObject;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<LogisticsCheckActionObject> get(@RequestParam(value = "orgId", required = true) String orgId,
                                                @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
                                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        LogisticsCheckActionObject[] objects =
                dataAPIClient.get("logisticscheckaction?orgId={orgId}&&pageIndex={pageIndex}&&pageSize={pageSize}",
                        LogisticsCheckActionObject[].class,
                        orgId,
                        pageIndex,
                        pageSize);

        if (objects == null)
            throw new NotFoundException("LogisticsCheckActionObject not found");

        return Arrays.asList(objects);
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void patch(@RequestBody LogisticsCheckActionObject logisticsCheckActionObject) {

        dataAPIClient.patch("logisticscheckaction", logisticsCheckActionObject);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") Integer id) {

        dataAPIClient.delete("logisticscheckaction/{id}", id);
    }
}
